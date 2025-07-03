export class JwtPayload {
  constructor(
    readonly sub: string,
    readonly exp: number,
    readonly player_id: number
  ) {}
}
