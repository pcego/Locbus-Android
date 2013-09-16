package br.com.kpc.locbus.util;

public class ConexaoServidor {

	// Link do web service
	private final String linkOnibus = "http://187.44.38.252:8080/locBus/rest/veiculos/buscaPorLinha/";
	private final String linkTodasParadas = "http://187.44.38.252:8080/locBus/rest/paradas/findAll";

	public String getLinkOnibus() {
		return linkOnibus;
	}

	public String getLinkTodasParadas() {
		return linkTodasParadas;
	}

	private static ConexaoServidor conexaoServidor;

	public static ConexaoServidor getConexaoServidor() {
		if (conexaoServidor == null) {
			conexaoServidor = new ConexaoServidor();
		}
		return conexaoServidor;
	}
}
