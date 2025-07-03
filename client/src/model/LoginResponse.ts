import type { s } from 'vitest/dist/reporters-yx5ZTtEV.js'

export class LoginResponse {
  constructor(
    readonly responseType: 'login',
    readonly login: Number,
    readonly token: string,
    readonly id: Number
  ) {}
}
