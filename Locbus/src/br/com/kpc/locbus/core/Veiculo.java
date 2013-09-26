package br.com.kpc.locbus.core;

public class Veiculo {

	private int _id;
	private String descricao;
	private String imei;
	private boolean status;
	private int empresa_id;
	private int linha_id;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getEmpresa_id() {
		return empresa_id;
	}

	public void setEmpresa_id(int empresa_id) {
		this.empresa_id = empresa_id;
	}

	public int getLinha_id() {
		return linha_id;
	}

	public void setLinha_id(int linha_id) {
		this.linha_id = linha_id;
	}

}
