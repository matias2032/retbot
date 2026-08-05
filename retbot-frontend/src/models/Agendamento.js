export default class Agendamento {
  constructor({
    idAgendamento,
    idContaSocial,
    tipo,
    idPublicacao,
    executarEm,
    prioridade,
    estado,
    tentativas,
    criadoEm
  }) {
    this.idAgendamento = idAgendamento;
    this.idContaSocial = idContaSocial;
    this.tipo = tipo;
    this.idPublicacao = idPublicacao;
    this.executarEm = executarEm ? new Date(executarEm) : null;
    this.prioridade = prioridade;
    this.estado = estado;
    this.tentativas = tentativas;
    this.criadoEm = criadoEm ? new Date(criadoEm) : null;
  }
}