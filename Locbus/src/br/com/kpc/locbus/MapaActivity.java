package br.com.kpc.locbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import br.com.kpc.locbus.util.InformacaoMaps;
import br.com.kpc.locbus.util.Mensagens;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends Activity implements LocationListener {

	private static final int RETORNO_MENU = 0;

	// Varial que vai grava lat long
	LatLng latLng = new LatLng(0, 0);
	// LatLng localizacao = new LatLng(49.187500, -122.849000);
	private GoogleMap map;

	@Override
	protected void onResume() {
		super.onResume();
		// Ligar GPS
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Desliga GPS //Testado - Funcionando
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa))
				.getMap();

		map.setMyLocationEnabled(true);
		InformacaoMaps informacaoMaps = new InformacaoMaps();

	}

	public void onClick_City(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		com.google.android.gms.maps.CameraUpdate update = CameraUpdateFactory
				.newLatLngZoom(localizacao(), 9);
		map.animateCamera(update);
	}

	public void onClick_Burnaby(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(localizacao(), 14);
		map.animateCamera(update);

	}

	public void onClick_Surrey(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(localizacao(), 16);
		map.animateCamera(update);

	}

	public void onClick_aki(View v) {

		// RECUPERAR INFORMAÇÃO DA TELA DE BUSCA MAPAS, QUE VAI DAR O RETORNO O
		// QUE O
		// USUARIO QUER FAZER

		startActivityForResult(new Intent(this, MapaBuscarActivity.class),
				RETORNO_MENU);
		// Intent i = new Intent(getApplicationContext(),
		// MapaBuscarActivity.class);
		// startActivity(i);

		// / Mensagens.instancia().testando();

		// map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// CameraUpdate update = CameraUpdateFactory
		// .newLatLngZoom(localizacao, 16);
		// map.setMyLocationEnabled(true);

	}

	@Override
	public void onLocationChanged(Location location) {
		// Centraliza o mapa nesta coordenada
		latLng = new LatLng(location.getLatitude(), location.getLongitude());
		// map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
		// Adicionar marcador

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int codigo, int resultado, Intent it) {
		// Retornos pode ser:
		// 1 - Onibus de determinada linha;
		// 2 - Parada de Onibus mais proxima;
		// 0 - RETORNO_MENU operação cancelada.

		Toast.makeText(this, "antrou " + resultado, Toast.LENGTH_SHORT).show();
		Bundle bundle = it != null ? it.getExtras() : null;
		String msg = bundle.getString("msg");

		//Retorno da opção do menu = 0
		if (codigo == RETORNO_MENU) {
			// 1 - Onibus de determinada linha;
			if (resultado == 1) {
				Toast.makeText(this, "SIM", Toast.LENGTH_SHORT).show();
				adicionarMarcador(localizacao());

			}// 2 - Parada de Onibus mais proxima;
			else if (resultado == 2) {
				Toast.makeText(this, "NAO", Toast.LENGTH_SHORT).show();
			}

		}

		super.onActivityResult(codigo, resultado, it);
	}

	public void adicionarMarcador(LatLng latLng) {

//		map.addMarker(new MarkerOptions().position(latLng).title(
//		"Find me here!"));
//		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
//		map.animateCamera(update);

		
		// // Cria um marcador
		 Marker frameworkSystem = map.addMarker(new MarkerOptions()
		 .position(latLng)
		 .title("Ônibus 1222")
		 .icon(BitmapDescriptorFactory
		 .fromResource(R.drawable.icon_locacao)));
		 // Move a câmera para Framework System com zoom 15.
		 map.moveCamera(CameraUpdateFactory.newLatLngZoom(
		 latLng, 7));
//		 map.animateCamera(CameraUpdateFactory.zoomTo(10), 1500, null);

	}
	
	public LatLng localizacao() {
		if (latLng.latitude == 0 || latLng.latitude == 0){
			Toast.makeText(this, "Aguardando as coordenadas...", Toast.LENGTH_SHORT).show();			
			return null;
		}
		return latLng; 
		
	}

}
