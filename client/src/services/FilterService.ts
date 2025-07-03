import type { GameGroupGame } from '@/model/Game'

export class FilterService {
  filterGames(allGames: GameGroupGame[], tags: TagSelection[]): GameGroupGame[] {
    return allGames.filter((game) => {
      return !tags.some((tag) => this.excludesGame(game, tag))
    })
  }

  private excludesGame(game: GameGroupGame, tagSelection: TagSelection): boolean {
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
