import {ObjectId} from "mongodb"

export class Semester {
    constructor(
        public name: string,
        public from: string,
        public to: string,
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
