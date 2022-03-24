import { ObjectId } from "mongodb";

export type NotificationTypes = "JoinRequest" | "JoinRequestHandled"

export class Notification {
    constructor(
        public type: NotificationTypes,
        public referencedResource?: ObjectId,
        public _id?: ObjectId
    ) {}
}