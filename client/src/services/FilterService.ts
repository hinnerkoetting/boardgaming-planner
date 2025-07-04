import type { RatedGame } from '@/model/Game'
import type { Player } from '@/model/Player/Player'

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

  filterWithStoredSettings(allGames: RatedGame[], allPlayers: Player[]): RatedGame[] {
    const filter = this.loadFilterSettings()
    if (!filter) {
      return allGames
    }
    return allGames.filter((game) => {
      return !this.excludesGame(game, filter, allPlayers)
    })
  }

  filterGames(allGames: RatedGame[], filter: FilterGamesSettings, allPlayers: Player[]): RatedGame[] {
    this.saveFilterSettings(filter)
    return allGames.filter((game) => {
      return !this.excludesGame(game, filter, allPlayers)
    })
  }

  private excludesGame(game: RatedGame, filter: FilterGamesSettings, allPlayers: Player[]): boolean {
    return (
      this.doesPlayerfilterTypeExcludeGame(game, filter) ||
      this.doesPlayingTimeExludeGame(game, filter) ||
      this.doNonPlayerTagsExcludeGame(game, filter) ||
      this.doPlayerTagsExcludeGame(game, filter, allPlayers) ||
      this.unratedDoesExcludeGame(game, filter)
    )
  }

  private doNonPlayerTagsExcludeGame(game: RatedGame, filter: FilterGamesSettings) {
    const tags: TagSelection[] = filter.nonPlayerTags
    return tags?.some((tag) => this.doesTagExcludeGame(game, tag))
  }

  private doPlayerTagsExcludeGame(game: RatedGame, filter: FilterGamesSettings, allPlayers: Player[]) {
    const tags: PlayerTagSelection[] = filter.playerTags
    return tags?.some((tag) => this.doesPlayerTagExcludeGame(game, tag, allPlayers))
  }

  private doesPlayingTimeExludeGame(game: RatedGame, filter: FilterGamesSettings) {
    if (!game.playingTimeMinutes) {
      return false
    }
    return (
      filter.minPlayingTime > (game.playingTimeMinutes || 0) ||
      filter.maxPlayingTime < (game.playingTimeMinutes || Number.MAX_VALUE)
    )
  }

  private doesPlayerfilterTypeExcludeGame(game: RatedGame, filter: FilterGamesSettings) {
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
    game: RatedGame,
    filter: FilterGamesSettings
  ) {
    if (!filter.numberOfPlayers) {
      return false
    }
    const numberOfPlayers = filter.numberOfPlayers
    const recommendedNumberOfPlayers = game.recommendedNumberOfPlayers
    return !recommendedNumberOfPlayers.includes(numberOfPlayers)
  }

  private doesNumberOfBestPlayersExcludeGame(game: RatedGame, filter: FilterGamesSettings) {
    if (!filter.numberOfPlayers) {
      return false
    }
    const numberOfPlayers = filter.numberOfPlayers
    const recommendedNumberOfPlayers = game.bestNumberOfPlayers
    return !recommendedNumberOfPlayers.includes(numberOfPlayers)
  }

  private doesNumberOfPlayersExcludeGame(game: RatedGame, filter: FilterGamesSettings) {
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

  private doesTagExcludeGame(game: RatedGame, tagSelection: TagSelection): boolean {
    if (tagSelection.selected === 'DO_NOT_FILTER') {
      return false
    }
    const gameContainsTag: boolean = game.tags.global.some((gameTag) => gameTag.id == tagSelection.id) || game.tags.group.some((gameTag) => gameTag.id == tagSelection.id)
    if (gameContainsTag && tagSelection.selected === 'FILTER_WITHOUT') {
      return true
    } else if (!gameContainsTag && tagSelection.selected === 'FILTER_WITH') {
      return true
    }
    return false
  }

  private doesPlayerTagExcludeGame(game: RatedGame, tagSelection: PlayerTagSelection, allPlayers: Player[]): boolean {
    if (tagSelection.selected === 'DO_NOT_FILTER') {
      return false
    }
    if (tagSelection.selected === 'FILTER_ANYONE') {
      return !game.tags.player.find((gameTag) => gameTag.id == tagSelection.id)
    } else if (tagSelection.selected === 'FILTER_EVERYONE') {
      const allStoredPlayerTagsForThisGame = game.tags.player.filter(t => t.id === tagSelection.id)
      // This should probably be enough, might lead to sync issues, but it's probably good enough to not compare all player ids.    
      return allStoredPlayerTagsForThisGame.length < allPlayers.length;
    } else if (tagSelection.selected == 'FILTER_NOBODY') {
      const allStoredPlayerTagsForThisGame = game.tags.player.filter(t => t.id === tagSelection.id)
      return allStoredPlayerTagsForThisGame.length > 0;
    }
    return false;    
  }

  private unratedDoesExcludeGame(game: RatedGame, filter: FilterGamesSettings): boolean {
    if (!filter.notRatedYet) {
      return false;
    }
    return !!game.rating.myRating;
  }
}

export type SelectionType = 'FILTER_WITH' | 'FILTER_WITHOUT' | 'DO_NOT_FILTER'
export type PlayerSelectionType = 'FILTER_EVERYONE' | 'FILTER_ANYONE' | 'FILTER_NOBODY' | 'DO_NOT_FILTER'

export class TagSelection {
  constructor(
    readonly description: string,
    readonly id: number,
    public selected: SelectionType
  ) {}
}

export class PlayerTagSelection {
  constructor(
    readonly description: string,
    readonly id: number,
    public selected: PlayerSelectionType
  ) {}
}

export class FilterGamesSettings {
  constructor(
    readonly nonPlayerTags: TagSelection[],
    readonly playerTags: PlayerTagSelection[],
    readonly numberOfPlayers: number | undefined,
    readonly minPlayingTime: number,
    readonly maxPlayingTime: number,
    readonly playerFilterType: PlayerFilterType,
    readonly notRatedYet: boolean
  ) {}
}
