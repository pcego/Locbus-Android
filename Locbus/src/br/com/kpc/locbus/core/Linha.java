package br.com.kpc.locbus.core;

public class Linha {
	private int _id;
	private String horaFinal;
	private String horaInicial;
	private int numeroLinha;
	private String pontoFinal;
	private String pontoInicial;
	private Boolean status;
	private String tipoLinha;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}

	public String getHoraInicial() {
		return horaInicial;
	}

	public void setHoraInicial(String horaInicial) {
		this.horaInicial = horaInicial;
	}

	public int getNumeroLinha() {
		return numeroLinha;
	}

	public void setNumeroLinha(int numeroLinha) {
		this.numeroLinha = numeroLinha;
	}

	public String getPontoFinal() {
		return pontoFinal;
	}

	public void setPontoFinal(String pontoFinal) {
		this.pontoFinal = pontoFinal;
	}

	public String getPontoInicial() {
		return pontoInicial;
	}

	public void setPontoInicial(String pontoInicial) {
		this.pontoInicial = pontoInicial;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getTipoLinha() {
		return tipoLinha;
	}

	public void setTipoLinha(String tipoLinha) {
		this.tipoLinha = tipoLinha;
	}

}
