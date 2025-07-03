export class JwtPayload {
  constructor(
    readonly sub: string,
    readonly exp: number,
    readonly player_id: number,
    readonly roles: Role[]
  ) {}
}

export enum Role {
  ADMIN = 'ROLE_ADMIN',
  USER = 'ROLE_USER'
}
