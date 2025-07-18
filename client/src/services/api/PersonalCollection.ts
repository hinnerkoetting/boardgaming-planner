import { wrapResponse, type ResponseWrapper } from "@/model/api/Response";
import type { GameGroup } from "@/model/GameGroup";
import { authorizedFetch } from "./ApiService";
import { getCurrentPlayerId } from "../LoginService";

export async function getPersonalCollection(): Promise<ResponseWrapper<GameGroup>> {
  const myPlayer = getCurrentPlayerId()
  const response = await authorizedFetch(`/api/players/${myPlayer}/personalCollection`)
  return await wrapResponse(response)
}

export async function createPersonalCollection(): Promise<ResponseWrapper<GameGroup>> {
  const myPlayer = getCurrentPlayerId()
  const response = await authorizedFetch(`/api/players/${myPlayer}/personalCollection`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  })
  return await wrapResponse(response)
}
