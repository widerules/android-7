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
        
        mapOverlays = mapView.getOverlays(); // MapView 객체에 정의된 오버레이 리스트 객체 참조
        myOverlay = new MyLocationOverlay(this, mapView); // 내 위치를 표시할 MyLocationOverlay 객체 생성
        mapOverlays.add(myOverlay); // MyLocationOverlay 객체를 오버레이 리스트에 추가
        

        
        startLocationService();
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		myOverlay.enableMyLocation(); // 액티비티가 화면에 보일 때 enableMyLocation() 메소드 호출
		
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		
		myOverlay.disableMyLocation(); // 액티비티가 중지 될 때 disableMyLocation() 메소드 호출
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