package br.com.kpc.locbus.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.kpc.locbus.R;
import br.com.kpc.locbus.core.Linha;

public class InformacaoLinhasPorParadaAdapter extends BaseAdapter {

	private ArrayList listData;
	private LayoutInflater layoutInflater;

	public InformacaoLinhasPorParadaAdapter(Context context, ArrayList listData) {
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
			convertView = layoutInflater.inflate(
					R.layout.itens_informacao_linha_por_parada, null);
			objCarregador = new ViewHolder();
			objCarregador.id = (TextView) convertView
					.findViewById(R.id.inforLinhaPorParadaID);
			objCarregador.numeroLinha = (TextView) convertView
					.findViewById(R.id.inforLinhaPorParadaNumeroLinha);
			objCarregador.tipo = (TextView) convertView
					.findViewById(R.id.inforLinhaPorParadaTipo);
			objCarregador.horaIni = (TextView) convertView
					.findViewById(R.id.inforLinhaPorParadaHoraIni);
			objCarregador.horaFim = (TextView) convertView
					.findViewById(R.id.inforLinhaPorParadaHoraFim);
			objCarregador.pontoIni = (TextView) convertView
					.findViewById(R.id.inforLinhaPorParadaPontoIni);
			objCarregador.pontoFim = (TextView) convertView
					.findViewById(R.id.inforLinhaPorParadaPontoFim);

			convertView.setTag(objCarregador);
		} else {
			objCarregador = (ViewHolder) convertView.getTag();
		}

		Linha linha = (Linha) listData.get(position);

		objCarregador.id.setText(Integer.toString(linha.get_id()));
		objCarregador.numeroLinha.setText(Integer.toString(linha
				.getNumeroLinha()));
		objCarregador.tipo.setText(linha.getTipoLinha());
		objCarregador.horaIni.setText(linha.getHoraInicial());
		objCarregador.horaFim.setText(linha.getHoraFinal());
		objCarregador.pontoIni.setText(linha.getPontoInicial());
		objCarregador.pontoFim.setText(linha.getPontoFinal());

		return convertView;
	}

	static class ViewHolder {
		TextView id;
		TextView numeroLinha;
		TextView tipo;
		TextView horaIni;
		TextView horaFim;
		TextView pontoIni;
		TextView pontoFim;

	}

}
