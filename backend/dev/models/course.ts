import {ObjectId} from "mongodb"

export class Course {
    constructor(
        public careers: string[],
        public name: string,
        public _id?: ObjectId
    ) {}
}

export class JoinCourse {
    constructor(
        public semesterId: ObjectId,
        public studentId: ObjectId,
        public courseId: ObjectId,
        public group: string,
        public status: "Pending" | "Declined" | "Accepted",
        public _id?: ObjectId
    ) {}
}
