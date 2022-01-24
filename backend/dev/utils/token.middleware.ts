import jwt from "jsonwebtoken"
import logger from "./logger"
import {NextFunction, Request, Response} from "express";

export const generateToken = (ci: string): string => {
    return jwt.sign({ci}, process.env.JWT_SECRET, { expiresIn: '12h' })
}

export const authenticateToken = async (req: Request, res: Response, next: NextFunction) => {
    const header = req.headers["authorization"]
    const token = header //&& header.split(' ')[1]

    if (token == null) return res.sendStatus(401)

    jwt.verify(token, process.env.JWT_SECRET, (err, payload) => {
        if (err) {
            logger.error(err)
            return res.status(403).json({message: "Sesion expirada."})
        }

        req.user = payload as {ci: string}
        next()
    })
}
