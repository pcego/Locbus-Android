package br.com.kpc.locbus.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.kpc.locbus.R;
import br.com.kpc.locbus.core.Linha;

public class LinhasAdapter extends BaseAdapter {
	private ArrayList listData;
	private LayoutInflater layoutInflater;

	public LinhasAdapter(Context context, ArrayList listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder objCarregador;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.itens_linhas, null);
			objCarregador = new ViewHolder();
			objCarregador.id = (TextView) convertView.findViewById(R.id.linhatvId);
			objCarregador.descricao = (TextView) convertView
					.findViewById(R.id.linhaTvDescricao);
			objCarregador.numeroLinhas = (TextView) convertView
					.findViewById(R.id.linhaTvNumeroLinha);
			convertView.setTag(objCarregador);
		} else {
			objCarregador = (ViewHolder) convertView.getTag();
		}

		Linha onibus = (Linha) listData.get(position);

		objCarregador.id.setText(Integer.toString(onibus.get_id()));
		objCarregador.descricao.setText(onibus.getPontoInicial() + " - " + onibus.getPontoFinal());
		objCarregador.numeroLinhas.setText(Integer.toString(onibus.getNumeroLinha()));

		return convertView;
	}

	static class ViewHolder {
		TextView id;
		TextView descricao;
		TextView numeroLinhas;
	}

}
