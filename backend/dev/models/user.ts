import {ObjectId} from "mongodb"

export class User {
    constructor(
        public name: string,
        public email: string,
        public career: string,
        public password: string,
        public ci: String,
        public role: string = "student",
        public _id?: ObjectId
    ) {
    }
}

export class UserShort {
    constructor(
        public name: string,
        public ci: String,
        public email: string,
        public _id?: ObjectId
    ) {
    }
}
