export class GameGroup {
  constructor(
    readonly id: number | undefined,
    readonly name: string
  ) {}
}

export class GameGroupStatistics {
  constructor(
    readonly playDates: number[]
  ) {}
}