/***
 * Copyright (c) 2010 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mapviewballoons.example.MyItemizedOverlay;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MyMapActivity extends MapActivity {

	MapView mapView;
	List<Overlay> mapOverlays;
	Drawable drawable;
	Drawable drawable2;
	MyItemizedOverlay itemizedOverlay;
	MyItemizedOverlay itemizedOverlay2;
	
	InputStreamReader isr;
	BufferedReader br;
	
	ArrayList<InfoMovieTheater> imtList;
	MovieTheater mt;
	
	MyLocationOverlay myOverlay;
	
	MapController mc;
	
	Parse parse;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymap);
        
        mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		mapOverlays = mapView.getOverlays();
		
		myOverlay = new MyLocationOverlay(this, mapView);
		mapOverlays.add(myOverlay);
		
		//	�� ��ġ ����
		drawable = getResources().getDrawable(R.drawable.marker);
		itemizedOverlay = new MyItemizedOverlay(drawable, mapView);
		
		//	��ȭ�� ��ġ ����
		drawable2 = getResources().getDrawable(R.drawable.marker2);
		itemizedOverlay2 = new MyItemizedOverlay(drawable2, mapView);
		
		parse = new Parse();
		
		mc = mapView.getController();
		mc.setZoom(15);
		
		
		startLocationService();
    }
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	 
	
	private void startLocationService() {

	    	// get manager instance
	    	LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


			// set listener
	    	GPSListener gpsListener = new GPSListener();
			long minTime = 10000;
			float minDistance = 0;

//			manager.requestLocationUpdates(
//						LocationManager.GPS_PROVIDER,
//						minTime,
//						minDistance,
//						gpsListener);

			// in case of network provider
			manager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER,
					minTime,
					minDistance,
					gpsListener);

			// get last known location first
			try {
				Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (lastLocation != null) {
					Double latitude = lastLocation.getLatitude();
					Double longitude = lastLocation.getLongitude();

//					Toast.makeText(getApplicationContext(), "Last Known Location : " + "Latitude : "+ latitude + "\nLongitude:"+ longitude, Toast.LENGTH_LONG).show();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}

//			Toast.makeText(getApplicationContext(), "Location Service started.\nyou can test using DDMS.", Toast.LENGTH_SHORT).show();

	    }
	
		public void markOverlay( InfoMovieTheater imt ) {
			try {
				String key = "AIzaSyC3gak9iNgS-OG19Z_OIrTKUjRORWxYMpU";	//	Ű
				String preUrl = "https://maps.googleapis.com/maps/api/place/details/json?reference="
								+ imt.getReference() + "&sensor=true&key=" + key;
				Log.d("������ �ּ�", preUrl);
				URL url = new URL(preUrl);
				isr = new InputStreamReader(url.openConnection().getInputStream(), "UTF-8");
				br = new BufferedReader(isr);
				
				mt = new MovieTheater();
				mt = parse.infoParse(br);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			GeoPoint point = new GeoPoint((int)(imt.getLat()*1E6),(int)(imt.getLng()*1E6));
			OverlayItem overlayItem = new OverlayItem(point, mt.getName(), 
					"�ּ� : " + mt.getAddr()
					+ "\n��ȭ��ȣ : " + mt.getTel() 
					);
			itemizedOverlay2.addOverlay(overlayItem);
			mapOverlays.add(itemizedOverlay2);
		}


		private class GPSListener implements LocationListener {

		    public void onLocationChanged(Location location) {
				//capture location data sent by current provider
				Double latitude = location.getLatitude();
				Double longitude = location.getLongitude();

				mapOverlays.clear();
				
				GeoPoint point = new GeoPoint((int)(latitude*1E6),(int)(longitude*1E6));
				OverlayItem overlayItem = new OverlayItem(point, "�� ��ġ", 
						"���� �ڽ��� ��ġ�Դϴ�.");
				itemizedOverlay.addOverlay(overlayItem);
				mapOverlays.add(itemizedOverlay);
				mc.animateTo(point);
				mc.setCenter(point);

				Log.d( "Google Maps", "���� : " + latitude + "�浵 : " + longitude + "���� : " + 10000);
				String key = "AIzaSyC3gak9iNgS-OG19Z_OIrTKUjRORWxYMpU";	//	Ű
				String preUrl = "https://maps.googleapis.com/maps/api/place/search/json?location="
								+ latitude + "," + longitude + "&radius=" + 10000 + "&types=movie_theater&sensor=false&key=" + key;
				
				Log.d("���� ��ġ", "���� : " + latitude + " �浵 : " + longitude);
				Log.d("�� �ε� �ּ�", preUrl);
				
				try {
					URL url = new URL(preUrl);
					isr = new InputStreamReader(url.openConnection().getInputStream(), "UTF-8");
					br = new BufferedReader(isr);
					imtList = new ArrayList<InfoMovieTheater>();
					imtList = parse.locationParse(br);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if( imtList != null ) {
					for( InfoMovieTheater imt : imtList ) {
						markOverlay(imt);
					}
				} else {
					Toast.makeText(getApplicationContext(), "�ݰ� 10Km �̳��� ��ȭ���� �����ϴ�.", Toast.LENGTH_SHORT).show();
				}
				
//				//	���� ��ġ �佺Ʈ �޽��� ����ֱ�
//				String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
//				Log.i("GPSListener", msg);
//				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}

		    public void onProviderDisabled(String provider) {
		    }

		    public void onProviderEnabled(String provider) {
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {
		    }

		}
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				finish();
			}
			return super.onKeyDown(keyCode, event);
		}

}
