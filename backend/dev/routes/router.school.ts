import express, {Request, Response} from "express";
import {getCourses, getSemesters} from "../controllers/controllers.school"
import { authenticateToken } from "../utils/token.middleware";

export const schoolRouter = express.Router()

schoolRouter.get('/semesters', getSemesters)

schoolRouter.get('/semesters/:semester/courses', authenticateToken, getCourses)
