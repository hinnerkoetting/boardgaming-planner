export class ErrorResponse {
  constructor(
    readonly responseType: 'error',
    readonly detail: string,
    readonly type: string
  ) {}
}
