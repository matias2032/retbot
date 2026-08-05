export default class Execucao {
  constructor({
    idExecucao,
    idAgendamento,
    iniciadoEm,
    terminadoEm,
    sucesso,
    codigoHttp,
    mensagem,
    requestId
  }) {
    this.idExecucao = idExecucao;
    this.idAgendamento = idAgendamento;
    this.iniciadoEm = iniciadoEm ? new Date(iniciadoEm) : null;
    this.terminadoEm = terminadoEm ? new Date(terminadoEm) : null;
    this.sucesso = sucesso;
    this.codigoHttp = codigoHttp;
    this.mensagem = mensagem;
    this.requestId = requestId;
  }
}