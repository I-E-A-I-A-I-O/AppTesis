import {ObjectId} from "mongodb"

export default class Semester {
    constructor(
        public name: String,
        public from: String,
        public to: String,
        public courses: {
            teacher: ObjectId,
            course: ObjectId,
            students: []
        },
        public _id?: ObjectId
    ) {}
}
