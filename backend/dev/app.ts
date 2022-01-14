import express from "express";
import path from "path";
import logger from "morgan";
import cors from "cors";
import helmet from "helmet";

import {router as indexRouter} from "./routes/index"
import {router as usersRouter} from "./routes/users"

export const app = express();

app.use(helmet());
app.use(cors());
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);
