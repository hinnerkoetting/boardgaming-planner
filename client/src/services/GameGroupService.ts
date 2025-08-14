import { GameGroupMember, GameGroupMemberType } from "@/model/GameGroupMember";

export function amIGroupAdminOrOwner(myPlayer: GameGroupMember): boolean {
  return myPlayer.type === GameGroupMemberType.ADMIN || myPlayer.type === GameGroupMemberType.OWNER
}