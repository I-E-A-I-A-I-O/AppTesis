import { ObjectId } from "mongodb";

export enum OperationType {
    Accept,
    Reject
}

export class TeacherHandleRequestBody {
    constructor(
        public requestId: string,
        public operationType: OperationType,
    ) {}
}