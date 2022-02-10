import {Request, Response} from "express"
import logger from "../utils/logger"
import { collections } from "../services/database.service"
import {Semester, SemesterCourse} from "../models/semesters"
import {User, UserShort} from "../models/user"
import { ObjectId } from "mongodb"
import Course from "../models/course"

export const getSemesters = async (req: Request, res: Response) => {
    try {
        const search = collections.semesters.find()
        const docs = await search.toArray() as Semester[]
        res.status(200).json({message: 'OK', semesters: docs})
    } catch(e) {
        logger.error(e)
        res.status(500).json({message: 'Error obteniendo los semestres.'})
    }
}

export const getCourses = async (req: Request, res: Response) => {
    try {
        const uSearch = await collections.users.findOne({ ci: req.user.ci }) as unknown as User
        
        if (!uSearch) {
            logger.info('failed courses GET due to CI not found')
            return res.status(404).json({message: "C.I no encontrada."})
        }

        const sSearch = await collections.semesters.findOne({_id: new ObjectId(req.params.semester)}) as Semester

        if (!sSearch) {
            logger.info('failed courses GET due to semester not found')
            return res.status(404).json({message: "Semestre no existe."})
        }

        let results: SemesterCourse[];

        if (req.user.role === 'teacher') {
            results = sSearch.courses.filter((semester) => {
                return semester.teacher === uSearch._id
            })
        }
        else {
            results = sSearch.courses.filter((semester) => {
                return semester.students.includes(uSearch._id)
            })
        }

        
        const courseIds = results.map((val) => { return val.course })
        const cSearch = collections.courses.find({_id: {$in: [...courseIds]}})
        const cSearchArr = await cSearch.toArray() as Course[]
        const coursesPromise = cSearchArr.map(async (c) => {
            const currentCourse = results[results.findIndex((v) => {v.course === c._id})]
            return {
                _id: c._id,
                name: c.name,
                teacher: await collections.users.findOne({ _id: currentCourse.teacher }) as unknown as UserShort,
                group: currentCourse.group
            }
        })

        const courses = await Promise.all(coursesPromise)

        res.status(200).json({
            message: 'OK',
            courses
        })
    } catch(e) {
        logger.error(e)
        res.status(500).json({message: 'Error obteniendo las materias.'})
    }
}
