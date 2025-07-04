import type { GameGroupGame } from '@/model/Game'

export type PlayerFilterType = 'OFF' | 'PLAYABLE' | 'RECOMMENDED' | 'BEST'

export class FilterService {
  loadFilterSettings(): FilterGamesSettings | undefined {
    const item = localStorage.getItem('filter-settings')
    if (!item) {
      return undefined
    }
    try {
      return JSON.parse(item)
    } catch (e) {
      console.error(e)
    }
    return undefined
  }

  saveFilterSettings(settings: FilterGamesSettings) {
    localStorage.setItem('filter-settings', JSON.stringify(settings))
  }

  filterWithStoredSettings(allGames: GameGroupGame[]): GameGroupGame[] {
    const filter = this.loadFilterSettings()
    if (!filter) {
      return allGames
    }
    return allGames.filter((game) => {
      return !this.excludesGame(game, filter)
    })
  }

  filterGames(allGames: GameGroupGame[], filter: FilterGamesSettings): GameGroupGame[] {
    this.saveFilterSettings(filter)
    return allGames.filter((game) => {
      return !this.excludesGame(game, filter)
    })
  }

  private excludesGame(game: GameGroupGame, filter: FilterGamesSettings): boolean {
    return (
      this.doesPlayerfilterTypeExcludeGame(game, filter) ||
      this.doesPlayingTimeExludeGame(game, filter) ||
      this.doTagsExcludeGame(game, filter)
    )
  }

  private doTagsExcludeGame(game: GameGroupGame, filter: FilterGamesSettings) {
    const tags: TagSelection[] = filter.tags
    return tags.some((tag) => this.doesTagExcludeGame(game, tag))
  }

  private doesPlayingTimeExludeGame(game: GameGroupGame, filter: FilterGamesSettings) {
    if (!game.playingTimeMinutes) {
      return false
    }
    return (
      filter.minPlayingTime > (game.playingTimeMinutes || 0) ||
      filter.maxPlayingTime < (game.playingTimeMinutes || Number.MAX_VALUE)
    )
  }

  private doesPlayerfilterTypeExcludeGame(game: GameGroupGame, filter: FilterGamesSettings) {
    switch (filter.playerFilterType) {
      case 'OFF':
        return false
      case 'PLAYABLE':
        return this.doesNumberOfPlayersExcludeGame(game, filter)
      case 'RECOMMENDED':
        return (
          this.doesNumberOfRecommendedPlayersExcludeGame(game, filter) &&
          this.doesNumberOfBestPlayersExcludeGame(game, filter)
        )
      case 'BEST':
        return this.doesNumberOfBestPlayersExcludeGame(game, filter)
    }
  }

  private doesNumberOfRecommendedPlayersExcludeGame(
    game: GameGroupGame,
    filter: FilterGamesSettings
  ) {
    if (!filter.numberOfPlayers) {
      return false
    }
    const numberOfPlayers = filter.numberOfPlayers
    const recommendedNumberOfPlayers = game.recommendedNumberOfPlayers
    return !recommendedNumberOfPlayers.includes(numberOfPlayers)
  }

  private doesNumberOfBestPlayersExcludeGame(game: GameGroupGame, filter: FilterGamesSettings) {
    if (!filter.numberOfPlayers) {
      return false
    }
    const numberOfPlayers = filter.numberOfPlayers
    const recommendedNumberOfPlayers = game.bestNumberOfPlayers
    return !recommendedNumberOfPlayers.includes(numberOfPlayers)
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
    const gameContainsTag = !!game.tags.global.find((gameTag) => gameTag.id == tagSelection.id) || !!game.tags.group.find((gameTag) => gameTag.id == tagSelection.id)
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
    readonly maxPlayingTime: number,
    readonly playerFilterType: PlayerFilterType
  ) {}
}
