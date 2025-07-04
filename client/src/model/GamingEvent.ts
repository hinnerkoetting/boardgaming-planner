export class GamingEvent {
  constructor(
    readonly id: number,
    public description: string,
    public start: number,
    public end: number    
  ) {}
}