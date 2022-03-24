import { ObjectId } from "mongodb";

export class TeacherHandleRequestBody {
    constructor(
        public requestId: string,
        public operationType: "Accept" | "Reject",
    ) {}
}