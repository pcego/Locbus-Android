package br.com.kpc.locbus.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.kpc.locbus.R;
import br.com.kpc.locbus.core.Veiculo;

public class OnibusAdapter extends BaseAdapter {

	private ArrayList listData;
	private LayoutInflater layoutInflater;

	public OnibusAdapter(Context context, ArrayList listData) {
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.itens_onibus, null);
			holder = new ViewHolder();
			holder.id = (TextView) convertView.findViewById(R.id.tvId);
			holder.descricao = (TextView) convertView
					.findViewById(R.id.tvDescricao);
			holder.empresa = (TextView) convertView
					.findViewById(R.id.tvEmpresa);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Veiculo veiculo = (Veiculo) listData.get(position);

		holder.id.setText(Integer.toString(veiculo.get_id()));
		holder.descricao.setText(veiculo.getDescricao());
		holder.empresa.setText(Integer.toString(veiculo.getEmpresa_id()));

		return convertView;
	}

	static class ViewHolder {
		TextView id;
		TextView descricao;
		TextView empresa;
	}

}
