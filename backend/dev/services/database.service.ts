import * as mongoDB from "mongodb"
import * as dotenv from "dotenv"
import logger from "../utils/logger"

export const collections: {
     users?: mongoDB.Collection,
     careers?: mongoDB.Collection,
     courses?: mongoDB.Collection,
     semesters?: mongoDB.Collection,
     joinRequests?: mongoDB.Collection
    } = {}

export async function connectToDatabase() {
    dotenv.config()
    const client: mongoDB.MongoClient = new mongoDB.MongoClient(process.env.DB_CONN_STRING)
    await client.connect()
    const db: mongoDB.Db = client.db(process.env.DB_NAME)
    logger.info(`Connected to database ${process.env.DB_NAME} successfully`)
    collections.users = db.collection(process.env.USERS_COLLECTION_NAME)
    logger.debug(`Connection to collection ${process.env.USERS_COLLECTION_NAME} successful`)
    collections.careers = db.collection(process.env.CAREERS_COLLECTION_NAME)
    logger.debug(`Connection to collection ${process.env.CAREERS_COLLECTION_NAME} successful`)
    collections.courses = db.collection(process.env.COURSES_COLLECTION_NAME)
    logger.debug(`Connection to collection ${process.env.COURSES_COLLECTION_NAME} successful`)
    collections.semesters = db.collection(process.env.SEMESTERS_COLLECTION_NAME)
    logger.debug(`Connection to collection ${process.env.SEMESTERS_COLLECTION_NAME} successful`)
    collections.joinRequests = db.collection(process.env.REQUESTS_COLLECTION_NAME)
    logger.debug(`Connection to collection ${process.env.REQUESTS_COLLECTION_NAME} successful`)
}
