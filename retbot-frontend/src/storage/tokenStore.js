/**
 * Guarda o access_token em memória (nunca em localStorage/sessionStorage).
 * Perde-se ao dar refresh na página — é esperado: nesse caso o
 * AuthContext usa o refresh_token (cookie httpOnly) para obter um novo.
 */
let accessToken = null;

export function getAccessToken() {
  return accessToken;
}

export function setAccessToken(token) {
  accessToken = token;
}

export function clearAccessToken() {
  accessToken = null;
}