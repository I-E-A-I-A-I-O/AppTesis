declare namespace Express {
    export interface Request {
        user?: {ci: string}
    }
}