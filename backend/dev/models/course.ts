import {ObjectId} from "mongodb"

export default class Course {
    constructor(
        public name: string,
        public _id?: ObjectId
    ) {}
}
