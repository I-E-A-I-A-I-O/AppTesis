import {Request, Response} from "express"
import { ObjectId } from "mongodb";
import logger from "../utils/logger"
import { collections } from "../services/database.service";
import User from "../models/user";
import bcrypt from "bcrypt"
import Login from "../models/login";
import {generateToken} from "../utils/token.middleware";

export const insertUser = async (req: Request, res: Response) => {
    try {
        const user = req.body as User;
        const search = (await collections.users.findOne({ $or: [{email: user.email}, {ci: user.ci}] })) as unknown as User

        if (search) {
            logger.info(`Account registration failed due to email already registered with id ${search.id}`)
            return res.status(400).json({message: "Correo o C.I ya se encuentran registrados."})
        }
        user.password = await bcrypt.hash(user.password, 10)
        const result = await collections.users.insertOne({...user, role: 'student'})

        if (result) {
            logger.info(`Successfully registered user with id ${result.insertedId}`)
            res.status(201).json({message: "Cuenta registrada exitosamente."})
        } else {
            logger.info(`Failed to register new user.`)
            res.status(500).json({message: "No se pudo registrar la cuenta."})
        }
    } catch (e) {
        logger.error(e)
        res.status(400).json({message: "Error registrando la cuenta."})
    }
}

export const userLogin = async (req: Request, res: Response) => {
    const login = req.body as Login
    const search = (await collections.users.findOne({ci: login.ci})) as unknown as User

    if (!search) {
        logger.info('failed login due to invalid CI')
        return res.status(404).json({message: "C.I no registrado."})
    }

    const same = await bcrypt.compare(login.password, search.password)

    if (!same) {
        logger.info(`failed login due to invalid password for ${login.ci}`)
        return res.status(403).json({message: "Contraseña incorrecta."})
    }

    const token = generateToken(login.ci)
    logger.info(`Login successful for ${login.ci}`)
    return res.status(201).json({
        message: "Inicio de sesion exitoso",
        token: token,
        email: search.email,
        name: search.name,
        ci: search.ci,
        role: search.role
    })
}
