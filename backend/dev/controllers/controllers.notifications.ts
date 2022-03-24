import { Request, Response } from "express"
import logger from "../utils/logger"
import { collections } from "../services/database.service"
import { ObjectId } from "mongodb";
import { NotificationTypes } from "../models/notification";

export const createNotification = async (type: NotificationTypes, referencedResource?: ObjectId) => {

}
