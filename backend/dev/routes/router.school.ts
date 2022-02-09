import express, {Request, Response} from "express";
import {getSemesters} from "../controllers/controllers.school"

export const schoolRouter = express.Router()

schoolRouter.get('/semesters', getSemesters)
