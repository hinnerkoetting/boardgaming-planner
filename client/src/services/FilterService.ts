import type { GameGroupGame } from '@/model/Game'

export class FilterService {
  filterGames(allGames: GameGroupGame[], filter: FilterGamesSettings): GameGroupGame[] {
    return allGames.filter((game) => {
      return !this.excludesGame(game, filter)
    })
  }

  private excludesGame(game: GameGroupGame, filter: FilterGamesSettings): boolean {
    return (
      this.doesNumberOfPlayersExcludeGame(game, filter) ||
      this.doesPlayingTimeExludeGame(game, filter) ||
      this.doTagsExcludeGame(game, filter)
    )
  }

  private doTagsExcludeGame(game: GameGroupGame, filter: FilterGamesSettings) {
    const tags: TagSelection[] = filter.tags
    return tags.some((tag) => this.doesTagExcludeGame(game, tag))
  }

  private doesPlayingTimeExludeGame(game: GameGroupGame, filter: FilterGamesSettings) {
    return (
      filter.minPlayingTime > (game.playingTimeMinutes || 0) ||
      filter.maxPlayingTime < (game.playingTimeMinutes || Number.MAX_VALUE)
    )
  }

  private doesNumberOfPlayersExcludeGame(game: GameGroupGame, filter: FilterGamesSettings) {
    if (!filter.numberOfPlayers) {
      return false
    }
    const numberOfPlayers = filter.numberOfPlayers
    const gameMinPlayers = game.minPlayers || 0
    if (gameMinPlayers > numberOfPlayers) {
      return true
    }
    const gameMaxPlayers = game.maxPlayers || Number.MAX_VALUE
    if (gameMaxPlayers < numberOfPlayers) {
      return true
    }
    return false
  }

  private doesTagExcludeGame(game: GameGroupGame, tagSelection: TagSelection): boolean {
    if (tagSelection.selected === 'DO_NOT_FILTER') {
      return false
    }
    const gameContainsTag = !!game.tags.find((gameTag) => gameTag.id == tagSelection.id)
    if (gameContainsTag && tagSelection.selected === 'FILTER_WITHOUT') {
      return true
    } else if (!gameContainsTag && tagSelection.selected === 'FILTER_WITH') {
      return true
    }
    return false
  }
}

export type SelectionType = 'FILTER_WITH' | 'FILTER_WITHOUT' | 'DO_NOT_FILTER'

export class TagSelection {
  constructor(
    readonly description: string,
    readonly id: number,
    public selected: SelectionType
  ) {}
}

export class FilterGamesSettings {
  constructor(
    readonly tags: TagSelection[],
    readonly numberOfPlayers: number | undefined,
    readonly minPlayingTime: number,
    readonly maxPlayingTime: number
  ) {}
}
