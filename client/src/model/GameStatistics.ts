export class GameStatistics {
  constructor(
    readonly lastPlayed: number | undefined,
    readonly playedNumberOfTimes: number,
    readonly numberOfTimesPlayedByPlayers: Map<number, number>,
    readonly playDates: number[]
  ) {    
  }
}