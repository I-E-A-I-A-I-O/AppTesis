import {Request, Response} from "express"
import logger from "../utils/logger"
import { collections } from "../services/database.service"
import Semester from "../models/semesters"

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
