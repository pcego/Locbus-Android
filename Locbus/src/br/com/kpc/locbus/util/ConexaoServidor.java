package br.com.kpc.locbus.util;

public class ConexaoServidor {

	//Link do web service
	private final String linkOnibus = "http://10.0.2.2:8080/locBus/rest/veiculos/buscaPorLinha/";

	public String getLinkOnibus() {
		return linkOnibus;
	}


	private static ConexaoServidor conexaoServidor;
	public static ConexaoServidor getConexaoServidor() {
		if (conexaoServidor == null) {
			conexaoServidor = new ConexaoServidor();
		}
		return conexaoServidor;
	}
}
