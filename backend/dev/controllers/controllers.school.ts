import { Request, Response } from "express";
import logger from "../utils/logger";
import { collections } from "../services/database.service";
import { Semester, SemesterCourse } from "../models/semesters";
import { User, UserShort } from "../models/user";
import { ObjectId } from "mongodb";
import { Course, JoinCourse } from "../models/course";
import { userLogin } from "./controllers.users";
import {
  TeacherHandleRequestBody,
  OperationType,
} from "../models/course.request";

export const getSemesters = async (req: Request, res: Response) => {
  try {
    const uSearch = (await collections.users.findOne({
      ci: req.user.ci,
    })) as unknown as User;

    if (!uSearch) {
      logger.warn("failed courses GET due to CI not found");
      return res.status(404).json({ message: "C.I no encontrada." });
    }

    const search = collections.semesters.find();
    const docs = (await search.toArray()) as Semester[];

    let filtered = docs.map((s) => {
      const courses = s.courses;

      if (req.user.role === "teacher") {
        const course = courses.find(
          (c) => JSON.stringify(c.teacher) === JSON.stringify(uSearch._id)
        );

        if (course) return s;
      } else {
        const course = courses.find((c) => {
          const student = c.students.find(
            (st) => JSON.stringify(st) === JSON.stringify(uSearch._id)
          );

          if (student) return c;
        });

        if (course) return s;
      }
    });

    filtered = filtered.filter((s) => {
      if (s) return s;
    });
    logger.warn(
      `semesters filtered for user ${uSearch._id.toString()} with role ${
        req.user.role
      }. Results: ${JSON.stringify(filtered)}`
    );
    res.status(200).json({ message: "OK", semesters: filtered });
  } catch (e) {
    logger.error(e);
    res.status(500).json({ message: "Error obteniendo los semestres." });
  }
};

export const getCourses = async (req: Request, res: Response) => {
  try {
    const uSearch = (await collections.users.findOne({
      ci: req.user.ci,
    })) as unknown as User;

    if (!uSearch) {
      logger.warn("failed courses GET due to CI not found");
      return res.status(404).json({ message: "C.I no encontrada." });
    }

    logger.warn(`GET semester started for ${req.params.semester}`);
    const sSearch = (await collections.semesters.findOne({
      _id: new ObjectId(req.params.semester),
    })) as Semester;
    logger.warn(
      `SEMESTER RETRIEVED. ALL COURSES: ${sSearch.courses.toString()}`
    );

    if (!sSearch) {
      logger.warn("failed courses GET due to semester not found");
      return res.status(404).json({ message: "Semestre no existe." });
    }

    let results: SemesterCourse[];

    if (req.user.role === "teacher") {
      results = sSearch.courses.filter(
        (semester) => semester.teacher.toString() === uSearch._id.toString()
      );
      logger.warn(
        `Filtered courses for teacher ${uSearch._id} in semester ${
          sSearch._id
        }. Results: ${results.toString()}`
      );
    } else {
      results = sSearch.courses.filter((semester) => {
        const sem = semester.students.find(
          (stu) => stu.toString() === uSearch._id.toString()
        );

        if (sem) return semester;
      });
    }

    const courseIds = results.map((val) => {
      return val.course;
    });
    logger.warn(`Filtered courses IDs. Result: ${courseIds.toString()}`);
    const cSearch = collections.courses.find({ _id: { $in: [...courseIds] } });
    const cSearchArr = (await cSearch.toArray()) as Course[];
    logger.warn(
      `GET for courses with IDs ${courseIds.toString()}. Result: ${JSON.stringify(cSearchArr)}`
    );
    const coursesPromise = results.map(async (c) => {
      const index = cSearchArr.findIndex(
        (v) => JSON.stringify(v._id) === JSON.stringify(c.course)
      );

      if (index !== -1) {
        const currentCourse = cSearchArr[index];
        return {
          _id: c.course,
          name: currentCourse.name,
          teacher: (await collections.users.findOne({
            _id: c.teacher,
          })) as unknown as UserShort,
          group: c.group,
        };
      }
    });

    const courses = await Promise.all(coursesPromise);
    logger.warn(
      `All courses filtered for user ${uSearch._id}. Results: ${JSON.stringify(courses)}`
    );

    res.status(200).json({
      message: "OK",
      courses,
    });
  } catch (e) {
    logger.error(e);
    res.status(500).json({ message: "Error obteniendo las materias." });
  }
};

export const getCurrentSemester = async (req: Request, res: Response) => {
  const uSearch = (await collections.users.findOne({
    ci: req.user.ci,
  })) as unknown as User;

  if (!uSearch) {
    logger.warn("failed courses GET due to CI not found");
    return res.status(404).json({ message: "C.I no encontrada." });
  }

  const currentDate = new Date();
  const search = collections.semesters.find();
  const docs = (await search.toArray()) as Semester[];
  const currentSemester = docs.find(
    (v) => new Date(v.from) <= currentDate && new Date(v.to) >= currentDate
  );

  if (currentSemester) {
    const requestSearch = collections.joinRequests.find({
      studentId: uSearch._id,
    });
    const requests = (await requestSearch.toArray()) as JoinCourse[];

    const unrequestedCoursesPromise = currentSemester.courses.map(async (c) => {
      const courseInfo = (await collections.courses.findOne({
        _id: c.course,
        careers: uSearch.career,
      })) as Course;

      if (courseInfo) {
        const requested = requests.find((r) => {
          return (
            JSON.stringify(r.studentId) === JSON.stringify(uSearch._id) &&
            JSON.stringify(r.courseId) === JSON.stringify(c.course)
          );
        });

        if (!requested) {
          const isInCourse = c.students.find(
            (st) => JSON.stringify(st) === JSON.stringify(uSearch._id)
          );

          if (!isInCourse)
            return {
              _id: courseInfo._id,
              name: courseInfo.name,
              teacher: c.teacher,
              group: c.group,
            };
        }
      }
    });

    const unrequestedCourses = await Promise.all(unrequestedCoursesPromise);

    logger.warn(
      `Succesfully filtered courses for user ${JSON.stringify(
        uSearch._id
      )} in the current semester. Results: ${JSON.stringify(
        unrequestedCourses
      )}`
    );

    return res.status(200).json({
      message: "OK",
      semesterId: currentSemester._id,
      semesterName: currentSemester.name,
      from: currentSemester.from,
      to: currentSemester.to,
      courses: unrequestedCourses,
    });
  } else {
    logger.warn(
      "Request failed with 404 because there's no active semester for the current date."
    );
    res
      .status(404)
      .json({ message: "No hay un semestre activo para este periodo." });
  }
};

export const requestCourseInvite = async (req: Request, res: Response) => {
  if (req.user.role !== "student")
    return res
      .status(401)
      .json({ message: "No tienes permiso para realizar esta operacion." });

  const uSearch = (await collections.users.findOne({
    ci: req.user.ci,
  })) as unknown as User;

  if (!uSearch) {
    logger.warn("failed courses GET due to CI not found");
    return res.status(404).json({ message: "C.I no encontrada." });
  }

  const { courseId } = req.params;

  try {
    const courseInfo = await collections.courses.findOne({
      _id: new ObjectId(courseId),
      careers: uSearch.career,
    });

    if (!courseInfo) {
      return res
        .status(404)
        .json({ message: `La materia con id ${courseId} no existe.` });
    }

    const currentDate = new Date();
    const search = collections.semesters.find();
    const docs = (await search.toArray()) as Semester[];
    const currentSemester = docs.find(
      (v) => new Date(v.from) <= currentDate && new Date(v.to) >= currentDate
    );

    if (!currentSemester) {
      logger.error(
        `Error creating new course join request for student with id ${uSearch._id} in course id ${courseId}`
      );
      return res
        .status(404)
        .json({ message: "No se pudo procesar la operacion." });
    }

    const courseFind = currentSemester.courses.find(
      (v) => JSON.stringify(v.course) === JSON.stringify(courseId)
    );

    if (!courseFind) {
      logger.error(
        `Error creating course join request for student with id ${uSearch._id} because course with id ${currentSemester._id} for the current semester`
      );
      return res
        .status(400)
        .json({ message: "Materia no existe para el trimestre actual" });
    }

    const requests = await collections.joinRequests.findOne({
      courseId: new Object(courseId),
      userId: uSearch._id,
      semesterId: currentSemester._id,
    });

    if (!requests) {
      return res.status(400).json({
        message: "Error, ya existe una invitacion para unirte a esta materia.",
      });
    }

    const request = new JoinCourse(
      currentSemester._id,
      uSearch._id,
      new ObjectId(courseId),
      "Pendiente"
    );

    collections.joinRequests.insertOne(request);

    return res
      .status(201)
      .json({ message: "Peticion para unirse al curso enviada!" });
  } catch (e) {
    logger.error(e);
    res.status(500).json({ message: "Error obteniendo las materias." });
  }
};

const handleInvite = async (req: Request, res: Response) => {
  if (req.user.role !== "teacher")
    return res
      .status(401)
      .json({ message: "No tienes permiso para realizar esta operacion." });

  const uSearch = (await collections.users.findOne({
    ci: req.user.ci,
  })) as unknown as User;

  if (!uSearch) {
    logger.warn("failed courses GET due to CI not found");
    return res.status(404).json({ message: "C.I no encontrada." });
  }

  const body = req.body as TeacherHandleRequestBody;
  const request = (await collections.joinRequests.findOne({
    _id: body.requestId,
  })) as JoinCourse;

  if (!request) {
    logger.error({
      message: `Error handling invite ${body.requestId} for teacher ${uSearch._id} because requests doesn't exist`,
    });
    return res
      .status(400)
      .json({ message: "No se pudo procesar la operacion." });
  }

  if (body.operationType === OperationType.Accept) {
    await collections.joinRequests.findOneAndUpdate(
      { _id: body.requestId },
      { status: "accepted" }
    );
    await collections.semesters.findOneAndUpdate(
      {
        _id: request.semesterId,
        "courses._id": request.courseId,
      },
      {}
    );
  } else {
  }
};
