import {ObjectId} from "mongodb"

export default class User {
    constructor(
        public name: string,
        public email: string,
        public career: string,
        public password: string,
        public ci: String,
        public role: string = "student",
        public id?: ObjectId
    ) {
    }
}
