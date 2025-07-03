import type { ErrorResponse } from '../ErrorResponse'

export type SuccessResponse<T extends {}> = T & { responseType: SucessResponseType }
export type ErrorResponseWrapper = ErrorResponse & { responseType: ErrorResponseType }
export type ResponseWrapper<T extends {}> = {
  success: T | undefined
  error: ErrorResponseWrapper | undefined
}

export async function wrapResponse<T extends {}>(response: Response): Promise<ResponseWrapper<T>> {
  if (response.status < 300) {
    const content: T = (await response.json()) as T
    return {
      success: content,
      error: undefined
    }
  }  
  const content = (await response.json()) as ErrorResponse

  return {
    success: undefined,
    error: content
  }
}

export async function wrapEmptySuccessResponse(
  response: Response
): Promise<ResponseWrapper<boolean>> {
  if (response.status < 300) {
    return {
      success: true,
      error: undefined
    }
  }
  const content = (await response.json()) as ErrorResponse

  return {
    success: undefined,
    error: content
  }
}

export type SucessResponseType = 'success'
export type ErrorResponseType = 'error'
