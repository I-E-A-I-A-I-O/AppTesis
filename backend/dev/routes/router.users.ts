import express, {Request, Response} from "express";
import {insertUser, userLogin} from "../controllers/controllers.users"
import {authenticateToken} from "../utils/token.middleware";
export const usersRouter = express.Router();

usersRouter.post('/', async (req: Request, res: Response) => {
    await insertUser(req, res)
})

usersRouter.get('/user/token', authenticateToken, async (req: Request, res: Response) => {
    res.status(200).json({message: ""})
})

usersRouter.post('/user', async (req: Request, res: Response) => {
    await userLogin(req, res)
})
