import express from "express";
import {
  getCourses,
  getCurrentSemester,
  getSemesters,
  requestCourseInvite,
  handleInvite
} from "../controllers/controllers.school";
import { authenticateToken } from "../utils/token.middleware";

export const schoolRouter = express.Router();

schoolRouter.get("/semesters", authenticateToken, getSemesters);

schoolRouter.get("/semesters/current", authenticateToken, getCurrentSemester);

schoolRouter.get("/semesters/:semester/courses", authenticateToken, getCourses);

schoolRouter.post(
  "/semesters/current/courses/:courseId/:group",
  authenticateToken,
  requestCourseInvite
);

schoolRouter.put("/requests/handle", authenticateToken, handleInvite);
