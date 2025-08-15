import type { GameGroup } from "@/model/GameGroup";
import { GameGroupMember, GameGroupMemberType } from "@/model/GameGroupMember";

export function amIGroupAdminOrOwner(myPlayer: GameGroupMember): boolean {
  return myPlayer.type === GameGroupMemberType.ADMIN || myPlayer.type === GameGroupMemberType.OWNER
}

export function storeCurrentGameGroup(gameGroup: GameGroup): void {
  localStorage.setItem('currentGameGroup', JSON.stringify(gameGroup));
}

export function removeCurrentGameGroup(): void {
  localStorage.removeItem('currentGameGroup');
}

export function getStoredCurrentGameGroup(): GameGroup | undefined {
  const storedValue = localStorage.getItem('currentGameGroup');
  return storedValue ? JSON.parse(storedValue) : undefined;
}
