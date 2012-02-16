package kr.co.WhenWhereWho3;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class BranchMapActivity extends MapActivity {

	private MapView mapView; // �ʺ� ��ü
	private List<Overlay> listOfOverlays; // �ʿ� ǥ�õ� ��������( ���̾� )���� ������ �ִ� �ҽ�
	private String bestProvider; // ���� ��ġ���� �������� ���� ���ι��̴�. ( network, gps )

	private LocationManager locM; // ��ġ �Ŵ���
	private LocationListener locL; // ��ġ ������

	private Location currentLocation; // ���� ��ġ
	private MapController mapController; // ���� �� ��Ű�ų�, �̵���Ű�µ� ���� ��Ʈ�ѷ�

	private LocationItemizedOverlay overlayHere; // ���� ��ġ ��Ŀ�� ǥ�õǾ��� ��������
	private LocationItemizedOverlay overlayBranch; // ���� ��ġ ��Ŀ���� ǥ�õǾ��� ��������
	private List<BranchInfoDTO> brList; // ���� ����Ʈ

	private String searchType; // 0�� ���� 1�� ATM
	private final String SEARCH_RANGE = "500"; // �˻� ���� =

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.branchmap); // �� ��Ƽ��Ƽ xml�� Ǯ����ģ��.

		overlayHere = null;
		overlayBranch = null; // �� �������� �ʱ�ȭ

		mapView = (MapView) findViewById(R.id.mapView); // �ʺ� ��ü�� �����´�.
		mapView.setBuiltInZoomControls(true); // �� ��, �� �ƿ� ��Ʈ���� ǥ���Ѵ�.

		mapController = mapView.getController(); // �� ��Ʈ�ѷ��� �����´�.
		mapController.setZoom(17); // �ʱ� Ȯ��� 17������ �Ѵ�.. ���߿� �ٲ㵵 ��.

		
		locM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		// ��� ������ ������ ���ι��̴��� �޾ƿ´�.
		// network( ���� 3G��, Wifi Ap ��ġ���� ) �Ǵ� gps ���� �ϳ��� �����ȴ�.
		bestProvider = locM.getBestProvider(new Criteria(), true);

		// ��⿡ ������ �ִ� ������ ��ġ������ ���� ��ġ�� �ʱ� �����Ѵ�.
		currentLocation = locM.getLastKnownLocation(bestProvider);

		// ��ġ ������ �ʱ�ȭ
		locL = new MyLocationListener();

		// ��ġ �Ŵ����� ��ġ �����ʸ� �����Ѵ�.
		// ��ġ �����ʿ��� 10000ms (10��) ���� 100���� �̻� �̵��� �߰ߵǸ� ������Ʈ�� �Ϸ� �Ѵ�.
		locM.requestLocationUpdates(bestProvider, 10000, 100, locL);

		// ó���� �ѹ� �ʺ信 �׷��ش�.
		updateOverlay(currentLocation);
	}// onCreate( ) ��

	// MyLocationListener�� ��Ƽ��Ƽ Ŭ���� ���� InnerŬ������.
	// �����ʴ� �����̼� �Ŵ����� �߰��Ǿ� GPS�� ��Ʈ��ũ�κ��� ��ġ���� ����Ǵ� ���� �����ϰ� �ȴ�.

	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// ��ġ �̵��� �߰ߵǾ��� �� ȣ��� �޼ҵ�,
			// ���� �������� 10�ʸ��� 100���� �̻� �̵��� �߰ߵǸ� ȣ��ȴ�.
			updateOverlay(location);

		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.d("GoogleMaps", "GPS disaled : " + provider);
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.d("GoogleMaps", "GPS enaled : " + provider);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d("GoogleMaps", "onStatusChanged : " + provider
					+ " & status = " + status);
		}
	}

	// ���� �׷��ִ� �޼���
	// ��û�� ������ Location ��ü(��ġ)�� �������� ���� ��ġ ��Ŀ�� ���, ���� ����Ʈ�� HttpClient�� ���Ͽ� ����ؼ�
	// �޾ƿ� �� �������� ��Ŀ�� ǥ���ϰ� �ȴ�.

	protected void updateOverlay(Location location) {
		// ���� ȭ�鿡 ���� ��������(��Ŀ��)�� �� �����.
		listOfOverlays = mapView.getOverlays(); // �ʺ信�� �������� ����Ʈ�� �����´�.

		if (listOfOverlays.size() > 0) {
			listOfOverlays.clear(); // �������̰� ���� �� �� �����ش�.
			Log.d("GoogleMaps", "clear overlays : " + listOfOverlays.size());
		} else {
			Log.d("GoogleMaps", "empty overlays");
		}

		// Location ��ü�� ������ GeoPoint ��ü�� ���� �޼ҵ�
		GeoPoint geoPoint = getGeoPoint(location);

		// ���� ��ġ�� ǥ���� �̹���
		Drawable marker;
		/*******************************************************************************/
		// ���� ��ҽ����� �б��Ͽ� ���� ��ġ�� ���� ��ġ �̹����� �����ϰ� �Ǿ��ִ�.
		// �����ؾ��� ����
		marker = getResources().getDrawable(R.drawable.ic_launcher);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		/*******************************************************************************/

		// LocatationItemizedOverlay�� �̿��Ͽ� ���� ��ġ ��Ŀ�� ���� �������̸� �����Ѵ�.
		overlayHere = new LocationItemizedOverlay(marker);
		// Touch Event�� null pointer ���׸� �����ϱ� ���� ��Ŀ�� ��� �ٷ� populate �����ش�.
		overlayHere.mPopulate();

		// ���� ��ġ�� GeoCoder�� �̿��Ͽ� �뷫�ּҿ� ��, �浵�� Toast�� ���Ͽ� �����ش�.
		String geoString = showNowHere(location.getLatitude(), location.getLongitude(), true);

		// ���� ��ġ ��Ŀ ����
		OverlayItem overlayItem = new OverlayItem(geoPoint, "here", geoString);
		overlayHere.addOverlay(overlayItem); // ������ġ �������� ����Ʈ�� ������ġ ��Ŀ�� �ִ´�.

		/********************************************************************************/
		// ���������� HTTP����� ���� �������� �޾ƿͼ� ���������� brList(���� ����Ʈ)�� �ִ´�.
		// ������ ����Ͽ� ������� ������ �Ǿ��ִ�.
		// �� ���� ��������Ʈ �������̿� �ְ� ȭ�鿡 ����ִ� �޼ҵ�,
		
		searchType = "0";
		
		showBranchMarker(location.getLatitude(), location.getLongitude(),
				this.searchType, SEARCH_RANGE);
//		showBranchMarker(location.getLatitude(), location.getLongitude(),
//				this.searchType, SEARCH_RANGE);

		// �ʺ信�� ��ġ�̺�Ʈ�� ���� �������̸� �߰��Ѵ�.
		// Ư�������� ���� ������ �� Ư�� ���� �������� ��˻��� �ϱ� ���Ͽ� ��ġ�̺�Ʈ�� �޾ƿ;� �Ѵ�.

		mapView.getOverlays().add(new MapTouchDetectorOverlay());

		// ���������� ������ �������̷��̾ �ʺ信 �߰��Ѵ�.
		mapView.getOverlays().add(overlayHere); // ��° ��ġ�� ȭ���� �̵��Ѵ�.
		mapView.getController().animateTo(geoPoint);
		mapView.postInvalidate(); // �ʺ並 �ٽ� �׷��ش�.
		// �̺κ��� �����ؾ���
		/********************************************************************************/
	}

	// ���� �����ϰ� �������ϰ� �����Ǿ� �־� �����ս��� ���� ��������. ������ ������ �ִ�.
	// �ð� ������ ������ ����...
	// updateOverlay �޼ҵ忡�� ���Ǿ��� getGeoPoint �޼ҵ� �����̴�.

	private GeoPoint getGeoPoint(Location location) {
		if (location == null) {
			return null;
		}
		// GeoPoint ��ü�� ����, �浵 ǥ�ÿ� 1E6�� ��������Ѵ�.
		Double lat = location.getLatitude() * 1E6;
		Double lng = location.getLongitude() * 1E6;
		return new GeoPoint(lat.intValue(), lng.intValue());
	}

	/*****************************************************************************************/
	//	����Ŭ���� LocationItemizedOverlay ����
	// ��Ŀ�� �����ϰ� �������̿� ǥ��, �׸��� ��Ŀ�� ������ �� �̺�Ʈ�� �߻���Ű�� Ŭ�����̴�.
	protected class LocationItemizedOverlay extends
			ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> overlays;

		public LocationItemizedOverlay(Drawable defaultMarker) { // �������� ������

			// ��Ŀ �̹����� ��� �Ʒ��κ��� ��Ŀ���� ǥ���ϴ� ����Ʈ�� �ǰ� �Ѵ�.
			super(boundCenterBottom(defaultMarker));
			overlays = new ArrayList<OverlayItem>();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return overlays.get(i);
		}

		@Override
		public int size() {
			return overlays.size();
		}

		public void addOverlay(OverlayItem overlay) {
			overlays.add(overlay);
			// null pointer ���׶����� �������� ������ �߰� �� ������ ���� populate ����� �Ѵ�.
			populate();
		}

		@Override
		protected boolean onTap(int index) {
			/*************************************************************/
			// ��Ŀ�� ������ �� �߻���ų �̺�Ʈ �޼ҵ�

			if ("here".equals(overlays.get(index).getTitle())) {
				// ���� ��ġ�� ��� ������ �佺Ʈ �޼����� �����ش�.
				Toast.makeText(getApplicationContext(),
						overlays.get(index).getSnippet(), Toast.LENGTH_SHORT)
						.show();
			} else {
				// ���� ������ ��� ���̾�α׸� ���Ͽ� ���� ������ �����ش�.
				// '��ȭ�ɱ�' ��ư���� �������� ��ȭ�Ŵ� ��ɵ� �߰��Ǿ��ִ�.
				// �ʺ信 ������ �ҽ��� �ƴϾ �̰������� ǥ������ �ʴ´�.
			}
			return true;
			/*************************************************************/
		}

		// �ܺο��� ��Ŀ�� populate�� ���ֱ� ���� �޼ҵ�.
		public void mPopulate() {
			populate();
		}
	}

	// ����Ŭ���� LocationItemizedOverlay ��
	/*****************************************************************************************/

	// ���� ������ HTTP ����� ���� �������� �޼��̴�.
	// HTTP ��Ž� �������� ���ֱ� ���� ������� ������ �غô�.
	// �ٵ� �����尡 �����Ѵ�� �������� �ʴ°� ����. �߸����� �ִ� ���ϱ�...

	private void showBranchMarker(double lat, double lng, String searchType,
			String searchRange) {
		GetMapDataThread excuteThread = new GetMapDataThread(getMapdataHandler,
				lat, lng, searchType, searchRange);
		excuteThread.start();
	}

	// ���� HTTP ����� �ϴ� Ŭ������ ȣ���ϴ� �������̴�.
	// HTTP ��� �κ��� ����ǥ�ÿ� ����� ���� ������ ���⼭ �ҽ��� �Խ������� �ʴ´�.
	// �ٸ� ������ HTTPConnection ���� �����Ǿ� �ִ� HTTP ����� HTTPClient�� �����ϴϱ�
	// �����ս��� �ξ� �������� ���ʿ��� Ŀ�ؼ��� ���� �� �־���.

	/*****************************************************************************************/
	//	����Ŭ���� GetMapDataThread ����( ������ )
	private class GetMapDataThread extends Thread {
		private Handler tHandler;

		private Double lat, lng;
		private String searchType;
		private String searchRange;

		public GetMapDataThread(Handler tHandler) {
			this.tHandler = tHandler;
		}

		public GetMapDataThread(Handler tHandler, Double lat, Double lng,
				String searchType, String searchRange) {
			this(tHandler); // ������ ó�� �Ϸ� �� ������ ������ ���� ������ ������ ��Ŀ�� ����� �ڵ鷯
			this.lat = lat; // ����
			this.lng = lng; // �浵
			/********************************************************************/
			this.searchType = searchType; // �˻����� (0 : ����, 1 : ATM )
			this.searchRange = searchRange; // �˻� ���� ������ m(����) �̴�.
			/********************************************************************/
		}

		@Override
		public void run() {
			Bundle bundle = new Bundle();
			try {
				// ���������� ������ ���� ����Ʈ�� �غ��Ѵ�. BranchInfoDTO�� �������̴�.
				brList = new ArrayList<BranchInfoDTO>();
//				brList = gdA.getMapData(lat.toString(), lng.toString(),
//						searchType, searchRange);
				/*********************************************************************/
				// gdA Ŭ������ HTTP ����� �ؼ� ���������� �������� Ŭ�����̴�.
				// ���⼭�� �������� �ʾҴ�. onCreate���� �����ߴ�.
				/*********************************************************************/
				bundle.putBoolean("SUCCESS_KEY", true); // �����ϸ� ���鿡 �����޼��� ����
			} catch (Exception e) {
				bundle.putBoolean("SUCCESS_KEY", false);// �����ϸ� false
				e.printStackTrace();
			} finally {
				try {
					Message msg = tHandler.obtainMessage();
					msg.setData(bundle);
					tHandler.sendMessage(msg); // �ڵ鷯�� �޼����� ������.
					interrupt();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	//	����Ŭ���� GetMapDataThread ����( ������ ) ��
	/*****************************************************************************************/

	final Handler getMapdataHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.getData().getBoolean("SUCCESS_KEY")) { // HTTP ����� ���������� �̷��
															// ������
				// draw branches
				Drawable branchMarker;

				int markerType = 0;

				if ("0".equals(searchType)) {
					/*****************************************/
					markerType = R.drawable.ic_launcher;
					/*****************************************/
				} else if ("1".equals(searchType)) {
					markerType = R.drawable.ic_launcher;
				}

				branchMarker = getResources().getDrawable(markerType);
				branchMarker.setBounds(0, 0, branchMarker.getIntrinsicWidth(),
						branchMarker.getIntrinsicHeight());
				Double lat, lng;

				// ���� ��Ŀ���� �׷��� �������̸� �غ��Ѵ�.
				overlayBranch = new LocationItemizedOverlay(branchMarker);
				overlayBranch.mPopulate();

				StringBuilder sb;
				// �ݺ����� ���鼭 ��Ŀ���� �������̿� �߰��Ѵ�.
				// ���߿� ��Ŀ�� ������ �� ���̾�α׿� ���� ������ �����ֱ� ���� �����꿡 ��� ������ String����
				// �����Ѵ�.

//				for (BranchInfoDTO d : brList) {
//					lat = Double.parseDouble(d.getYCord()) * 1E7;
//					lng = Double.parseDouble(d.getXCord()) * 1E6;
//					GeoPoint branchGeoPoint = new GeoPoint(lat.intValue(),
//							lng.intValue());
//
//					sb = new StringBuilder();
//					sb.append(d.getBussBrNm()).append(";")
//							.append(d.getBussBrTelNo()).append(";")
//							.append(d.getBussBrAdr()).append(";")
//							.append(d.getTrscDrtm()).append(";")
//							.append(d.getBussBrAdr2());
//
//					// Create new overlay with marker at geoPoint
//					OverlayItem overlayItem = new OverlayItem(branchGeoPoint,
//							"branch", sb.toString());
//					overlayBranch.addOverlay(overlayItem);
//				}
			}

			// ��Ŀ �������� ������ ���� �޽����� �佺Ʈ�� �����ش�.
			if (overlayBranch.size() < 1) {
				Toast.makeText(
						getApplicationContext(),
						"�˻������ ���ų� ��� ��� �Դϴ�. \n'�޴�'��ư�� ���� ������ �����Ͽ� �ٽ� �˻��� �ּ���.",
						Toast.LENGTH_LONG).show();
			}

			// ���� �������̸� �ʺ� �������̿� ���������� �߰��� �ش�.
			if (overlayBranch != null) {
				mapView.getOverlays().add(overlayBranch);
				mapView.postInvalidate();
			}
		};
	};

	// �佺Ʈ �޼����� ���� �ּҿ� ����, �浵�� ��� ǥ�����ִ� �޼ҵ�.

	private String showNowHere(double lat, double lng, boolean showOption) {
		StringBuilder geoString = new StringBuilder();
		try {
			Geocoder geocoder = new Geocoder(getApplicationContext(),
					Locale.getDefault());
			Address adr = geocoder.getFromLocation(lat, lng, 1).get(0);

			if (adr.getLocality() != null)
				geoString.append(adr.getLocality()).append(" ");
			if (adr.getThoroughfare() != null)
				geoString.append(adr.getThoroughfare());
			if (!"".equals(geoString.toString()))
				geoString.append("\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

		geoString.append("���� : ").append(lat).append(" , �浵 : ").append(lng);

		if (showOption) {
			Toast.makeText(getApplicationContext(), geoString.toString(),
					Toast.LENGTH_SHORT).show();
		}
		return geoString.toString();
	}

	// �� ������ ȭ�鿡�� ��ġ �̺�Ʈ�� �޾ƿ� ���������̴�.
	// �ʺ信�� Ư�� ������ ������ ������ ���� ��ġ�� �ƴ� Ư�������� �������� ���������� �˻��ؿ����� ����
	// ���������ε� ��� ������ �̺�Ʈ�� �޾ƿ��� ����� �� ������̴�.
	// �и� �� �κ��� ������ �Ǿ�� �� ���̴�.

	/*****************************************************************************/
	// �̺κ� �ʿ� ���Ѱ� �������̽� �޾ƾߵǴ°� ������ ��������?
	public class MapTouchDetectorOverlay extends Overlay implements
			OnGestureListener {
		private GestureDetector gestureDetector;

		// ontouchEvent�� ACTION_DOWN ���� ������ ���� ó������ �ʰ�
		// ����ó���� ���� ĳġ�� �� �ִ� �������̴�.
		private OnGestureListener onGestureListener;
		// 1.5�ʸ� ��� �������� �ν��Ѵ�.
		private static final long LOOOOONG_PRESS_MILLI_SEC = 1500;

		// for touch timer
		private Handler mHandler;
		private long touchStartTime;
		private long longPressTime;
		private MotionEvent globalEvent;

		// ������
		public MapTouchDetectorOverlay() {
			gestureDetector = new GestureDetector(this);
			init();
		}

		public MapTouchDetectorOverlay(OnGestureListener onGestureListener) {
			this();
			setOnGestureListener(onGestureListener);
			init();
		}

		// �����ڵ��� ȣ���� �ʱ�ȭ �Լ�
		private void init() {
			mHandler = new Handler();
			globalEvent = null;
		}

		// ��� ������ ������ ������
		private Runnable loongPressDetector = new Runnable() {

			@Override
			public void run() {
				// ȭ���� ������ �ִ� �ð�
				long touchHoldTime = longPressTime - touchStartTime;

				// 200ms�� ���ְ� �˻��ϴ°� ��⸶�� ������ �޶� �ణ�� ������ �ذ�
				if ((globalEvent != null)
						&& (touchHoldTime > (LOOOOONG_PRESS_MILLI_SEC - 200))) {
					Log.d("GoogleMaps", "loooooong press detected!");

					// ȭ�鿡�� �����ִ� ������ �޾ƿ´�.
					float x = globalEvent.getX();
					float y = globalEvent.getY();

					// �����ִ� ������ ����, �浵�� �ٲ��ش�.
					GeoPoint p = mapView.getProjection().fromPixels((int) x,
							(int) y);

					Location selectedLocation = new Location(currentLocation);

					selectedLocation.setLatitude((p.getLatitudeE6() / 1E6));
					selectedLocation.setLongitude((p.getLongitudeE6() / 1E6));

					currentLocation = selectedLocation;

					locM.removeUpdates(locL); // ���� ��ġ �����ʸ� ��� ���ֹ�����.
					updateOverlay(currentLocation); // ���� ��˻� �� ��Ŀ �ٽ� ǥ����.

					showNowHere((p.getLatitudeE6() / 1E6),
							(p.getLongitudeE6() / 1E6), true);
				}
			}
		};

		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			if (gestureDetector.onTouchEvent(event)) {
				return true;
			}
			onLongPress(event);
			return false;
		};

		@Override
		public boolean onDown(MotionEvent e) {
			if (onGestureListener != null) {
				return onGestureListener.onDown(e);
			} else {
				// Start Timer
				touchStartTime = System.currentTimeMillis();
				// 1.5�� �ִٰ� ��� ������ üũ�� ����.
				mHandler.postDelayed(loongPressDetector,
						LOOOOONG_PRESS_MILLI_SEC);
			}
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (onGestureListener != null) {
				onGestureListener.onFling(e1, e2, velocityX, velocityY);
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			if (onGestureListener != null) {
				onGestureListener.onLongPress(e);
			}

			// ȭ���� ������ ������ onLongPress�� ȣ��Ǵ� �� ȣ��ɶ����� üũ�� �ð��� ������ �ִ´�.
			// �� �κ��� �����ս� �϶��� ������ �� �� ���ܴ�.
			globalEvent = e;
			longPressTime = System.currentTimeMillis();
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (onGestureListener != null) {
				onGestureListener.onScroll(e1, e2, distanceX, distanceY);
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			if (onGestureListener != null) {
				onGestureListener.onShowPress(e);
			}

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (onGestureListener != null) {
				onGestureListener.onSingleTapUp(e);
			}
			return false;
		}

		public boolean isLongpressEnabled() {
			return gestureDetector.isLongpressEnabled();
		}

		public void setIsLongpressEnabled(boolean isLongpressEnabled) {
			gestureDetector.setIsLongpressEnabled(isLongpressEnabled);
		}

		public OnGestureListener getOnGestureListener() {
			return onGestureListener;
		}

		public void setOnGestureListener(OnGestureListener onGestureListener) {
			this.onGestureListener = onGestureListener;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
