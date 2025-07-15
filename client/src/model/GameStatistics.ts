export class GameStatistics {
  constructor(
    readonly lastPlayed: number | undefined,
    readonly playedNumberOfTimes: number,
    readonly numberOfTimesPlayedByPlayers: [string: number],
    readonly playDates: number[]
  ) {    
  }
}