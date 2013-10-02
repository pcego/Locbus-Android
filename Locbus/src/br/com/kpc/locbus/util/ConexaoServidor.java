package br.com.kpc.locbus.util;

public class ConexaoServidor {
	//Servidor casa       http://187.44.38.252
	//Servidor restaurante http://187.44.47.206/
	// Link do web service
	private final String linkVeiculosPorLinha = "http://187.44.47.206/locBus/rest/veiculos/buscaPorLinha/";
	private final String linkParadasTodas = "http://187.44.47.206/locBus/rest/paradas/findAll";
	private final String linkParadasPorRua = "http://187.44.47.206/locBus/rest/paradas/getPorRua/";
	private final String linkParadasPorBairro = "http://187.44.47.206/locBus/rest/paradas/getPorBairro/";

	private final String linkTodaslinhas = "http://187.44.47.206/locBus/rest/linhas/findAll/";
	private final String linkUltimaPosicaoImei = "http://187.44.47.206/locBus/rest/posicoes/getUltimaPosicao/";
	
	
	
	public String getLinkVeiculosPorLinha() {
		return linkVeiculosPorLinha;
	}

	public String getLinkParadasTodas() {
		return linkParadasTodas;
	}

	public String getLinkTodaslinhas() {
		return linkTodaslinhas;
	}

	public String getLinkUltimaPosicaoImei() {
		return linkUltimaPosicaoImei;
	}



	public String getLinkParadasPorRua() {
		return linkParadasPorRua;
	}

	public String getLinkParadasPorBairro() {
		return linkParadasPorBairro;
	}



	private static ConexaoServidor conexaoServidor;

	public static ConexaoServidor getConexaoServidor() {
		if (conexaoServidor == null) {
			conexaoServidor = new ConexaoServidor();
		}
		return conexaoServidor;
	}
}
