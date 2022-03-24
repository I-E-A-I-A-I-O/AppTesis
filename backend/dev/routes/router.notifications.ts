import express from "express";
import {
  getCourses,
  getCurrentSemester,
  getSemesters,
  requestCourseInvite,
  handleInvite
} from "../controllers/controllers.school";
import { authenticateToken } from "../utils/token.middleware";

export const notificationsRouter = express.Router();


