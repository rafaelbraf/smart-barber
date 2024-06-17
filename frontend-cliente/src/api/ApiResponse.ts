export interface ApiResponse<T> {
    statusCode: number;
    success: boolean;
    message: string;
    result: T;
}