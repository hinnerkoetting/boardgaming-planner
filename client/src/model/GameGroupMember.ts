export class GameGroupMember {
  constructor(
    readonly id: number,
    readonly name: String,
    public type: GameGroupMemberType
  ) { }
}

export enum GameGroupMemberType {
  ADMIN = 'ADMIN',
  MEMBER = 'MEMBER',
  OWNER = 'OWNER'
}

export function memberTypeToString(type: GameGroupMemberType): string {
  switch (type) {
    case GameGroupMemberType.ADMIN:
      return 'Admin';
    case GameGroupMemberType.MEMBER:
      return 'Member';
    case GameGroupMemberType.OWNER:
      return 'Owner';
    default:
      return 'Unknown';
  }
}