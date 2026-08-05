export default class RateLimit {
  constructor({
    idContaSocial,
    endpoint,
    limite,
    restante,
    reiniciaEm
  }) {
    this.idContaSocial = idContaSocial;
    this.endpoint = endpoint;
    this.limite = limite;
    this.restante = restante;
    this.reiniciaEm = reiniciaEm ? new Date(reiniciaEm) : null;
  }
}