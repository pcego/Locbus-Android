package br.com.kpc.locbus.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Mensagens extends Activity {
	Context contexto = getApplicationContext();
	String texto = "exemplo toast";
	int duracao = Toast.LENGTH_SHORT;

	private static Mensagens instancia;

	public Mensagens() {
	}

	public static Mensagens instancia() {
		if (instancia == null) {
			instancia = new Mensagens();
		}
		return instancia;
	}

	public void testando() {

//		Toast toast = Toast.makeText(getApplicationContext(), "safds",
//				Toast.LENGTH_LONG);
//		toast.show();
//		
		Log.d("Mensagem", "Entrou no testando");

	}

}
