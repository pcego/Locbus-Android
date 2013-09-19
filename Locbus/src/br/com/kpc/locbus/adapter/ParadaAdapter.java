package br.com.kpc.locbus.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.kpc.locbus.R;
import br.com.kpc.locbus.core.Parada;

public class ParadaAdapter extends BaseAdapter{
	
	private ArrayList listData;
	private LayoutInflater layoutInflater;

	public ParadaAdapter(Context context, ArrayList listData) {
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
			convertView = layoutInflater.inflate(R.layout.itens_paradas, null);
			objCarregador = new ViewHolder();
			objCarregador.id = (TextView) convertView.findViewById(R.id.paradaTvid);
			objCarregador.descricao = (TextView) convertView
					.findViewById(R.id.paradaTvDescricao);
			objCarregador.endereco = (TextView) convertView
					.findViewById(R.id.paradaTvEndereco);
			convertView.setTag(objCarregador);
		} else {
			objCarregador = (ViewHolder) convertView.getTag();
		}

		Parada parada = (Parada) listData.get(position);

		objCarregador.id.setText(Integer.toString(parada.get_id()));
		objCarregador.descricao.setText(parada.getDescricao());
	//	objCarregador.endereco.setText(parada.getRua() +" - "+ parada.getBairro());
			objCarregador.endereco.setText(parada.getLatitude());

		return convertView;
	}

	static class ViewHolder {
		TextView id;
		TextView descricao;
		TextView endereco;
	}

}
