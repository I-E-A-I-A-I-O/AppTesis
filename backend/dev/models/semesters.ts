import {ObjectId} from "mongodb"

export class Semester {
    constructor(
        public name: String,
        public from: String,
        public to: String,
        public courses: SemesterCourse[],
        public _id?: ObjectId
    ) {}
}

export class SemesterCourse {
    constructor(
        public teacher: ObjectId,
        public course: ObjectId,
        public group: string,
        public students: ObjectId[]
    ) {}
}
