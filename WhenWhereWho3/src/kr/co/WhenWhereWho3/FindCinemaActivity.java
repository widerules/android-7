package kr.co.WhenWhereWho3;

import java.util.List;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class FindCinemaActivity extends MapActivity {
	MapView mapView;
	List<Overlay> mapOverlays;
	MyLocationOverlay myOverlay;
	boolean mCompassEnabled; 
	SensorManager mSensorManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findcinema); 
        
        mapView = (MapView)findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        
        mapOverlays = mapView.getOverlays(); // MapView ��ü�� ���ǵ� �������� ����Ʈ ��ü ����
        myOverlay = new MyLocationOverlay(this, mapView); // �� ��ġ�� ǥ���� MyLocationOverlay ��ü ����
        mapOverlays.add(myOverlay); // MyLocationOverlay ��ü�� �������� ����Ʈ�� �߰�
        

        
        startLocationService();
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		myOverlay.enableMyLocation(); // ��Ƽ��Ƽ�� ȭ�鿡 ���� �� enableMyLocation() �޼ҵ� ȣ��
		
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		
		myOverlay.disableMyLocation(); // ��Ƽ��Ƽ�� ���� �� �� disableMyLocation() �޼ҵ� ȣ��
	}


	private void startLocationService() {

    	// get manager instance
    	LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// set listener
    	GPSListener gpsListener = new GPSListener();
		long minTime = 10000;
		float minDistance = 0;

		manager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					minTime,
					minDistance,
					gpsListener);

		Toast.makeText(getApplicationContext(), "Location Service started.\nyou can test using DDMS.", Toast.LENGTH_SHORT).show();
    }
	private class GPSListener implements LocationListener {

	    public void onLocationChanged(Location location) {
			//capture location data sent by current provider
			Double latitude = location.getLatitude();
			Double longitude = location.getLongitude();

			String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
			Log.i("GPSLocationService", msg);
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

			showCurrentLocation(latitude, longitude);

		}

	    public void onProviderDisabled(String provider) {
	    }

	    public void onProviderEnabled(String provider) {
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }

	}


	private void showCurrentLocation(Double latitude, Double longitude) {
		double intLatitude = latitude.doubleValue() * 1000000;
		double intLongitude = longitude.doubleValue() * 1000000;

		// new GeoPoint to be placed on the MapView
		GeoPoint geoPt = new GeoPoint((int) intLatitude, (int) intLongitude);

		MapController controller = mapView.getController();
		controller.animateTo(geoPt);

		int maxZoomlevel = mapView.getMaxZoomLevel();
		int zoomLevel = (int) ((maxZoomlevel + 1)/1.15);
		controller.setZoom(17);
		controller.setCenter(geoPt);

		mapView.setSatellite(true);
		mapView.setTraffic(false);

	}
	
}