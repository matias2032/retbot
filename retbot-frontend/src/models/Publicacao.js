export default class Publicacao {
  constructor({
    idPublicacao,
    idContaSocial,
    idPublicacaoExterna,
    texto,
    publicadoEm,
    criadoEm
  }) {
    this.idPublicacao = idPublicacao;
    this.idContaSocial = idContaSocial;
    this.idPublicacaoExterna = idPublicacaoExterna;
    this.texto = texto;
    this.publicadoEm = publicadoEm ? new Date(publicadoEm) : null;
    this.criadoEm = criadoEm ? new Date(criadoEm) : null;
  }
}