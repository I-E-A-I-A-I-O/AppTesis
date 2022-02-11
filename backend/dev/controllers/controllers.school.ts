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
            logger.warn('failed courses GET due to CI not found')
            return res.status(404).json({message: "C.I no encontrada."})
        }

        logger.warn(`GET semester started for ${req.params.semester}`)
        const sSearch = await collections.semesters.findOne({_id: new ObjectId(req.params.semester)}) as Semester
        logger.warn(`SEMESTER RETRIEVED. ALL COURSES: ${JSON.stringify(sSearch.courses)}`)

        if (!sSearch) {
            logger.warn('failed courses GET due to semester not found')
            return res.status(404).json({message: "Semestre no existe."})
        }

        let results: SemesterCourse[];

        if (req.user.role === 'teacher') {
            results = sSearch.courses.filter((semester) => {
                return JSON.stringify(semester.teacher) === JSON.stringify(uSearch._id)
            })
            
            logger.warn(`Filtered courses for teacher ${uSearch._id} in semester ${sSearch._id}. Results: ${JSON.stringify(results)}`)
        }
        else {
            results = sSearch.courses.filter((semester) => {
                return semester.students.includes(uSearch._id)
            })
        }

        
        const courseIds = results.map((val) => { return val.course })
        logger.warn(`Filtered courses IDs. Result: ${JSON.stringify(courseIds)}`)
        const cSearch = collections.courses.find({_id: {$in: [...courseIds]}})
        const cSearchArr = await cSearch.toArray() as Course[]
        logger.warn(`GET for courses with IDs ${JSON.stringify(courseIds)}. Result: ${JSON.stringify(cSearchArr)}`)
        const coursesPromise = results.map(async (c) => {
            const index = cSearchArr.findIndex((v) => {
                return JSON.stringify(v._id) === JSON.stringify(c.course)
            })

            if (index !== -1) {
                const currentCourse = cSearchArr[index]
                return {
                    _id: c.course,
                    name: currentCourse.name,
                    teacher: await collections.users.findOne({ _id: c.teacher }) as unknown as UserShort,
                    group: c.group
                }
            }
        })

        const courses = await Promise.all(coursesPromise)
        logger.warn(`All courses filtered for user ${uSearch._id}. Results: ${JSON.stringify(courses)}`)

        res.status(200).json({
            message: 'OK',
            courses
        })
    } catch(e) {
        logger.error(e)
        res.status(500).json({message: 'Error obteniendo las materias.'})
    }
}
