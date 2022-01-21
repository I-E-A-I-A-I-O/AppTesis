import express, {Request, Response} from "express";
import {insertUser} from "../controllers/controllers.users"
export const usersRouter = express.Router();

usersRouter.post('/', async (req: Request, res: Response) => {
    await insertUser(req, res)
})
