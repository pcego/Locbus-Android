package br.com.kpc.locbus.util;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.model.LatLng;

public class InformacaoMaps implements LocationSource {
	OnLocationChangedListener listener;
	

	@Override
	public void activate(OnLocationChangedListener listener) {
		// A localização foi ativa
		this.listener = listener;

	}

	@Override
	public void deactivate() {
		// Minha localização atual desativada
		this.listener = null;

	}

	public void setLocation(LatLng latLng) {
		Location location = new Location(LocationManager.GPS_PROVIDER);
		location.setLatitude(latLng.latitude);
		location.setLongitude(latLng.longitude);
		if (this.listener != null) {
			this.listener.onLocationChanged(location); // recebendo nova LatLng
		}
	}
}
