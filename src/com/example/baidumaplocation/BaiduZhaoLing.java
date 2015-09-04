package com.example.baidumaplocation;

import tx.ydd.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class BaiduZhaoLing extends Activity {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	// ��λ���
	LocationClient mLocClient;// ��λSDK�ĺ�����
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	BDLocation location;

	private double longitude;// Longitude��Latitude�����ֶ���λ�ҵ�λ��
	private double latitude;
	private Button shi;// �������İ�ť
	private ImageView dingwei, foundlocation;// �����ȡ��ǰ��λ���꣬�ҵ�λ�ð�ť�������ȡ���������ڵ�λ��
	// ���������
	private BitmapDescriptor mMarker;
	// �༭�򲼾�,��ʼ��ʱ���ظò���
	private RelativeLayout relativeLayout;

	// ��Ʒ�ľ�γ��
	Double latitude1;
	Double longitude1;

	private CheckBox checkBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.baidu_activity_main_lost);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfigeration(
				mCurrentMode, true, mCurrentMarker));

		// ���ö�λͼ������ã���λģʽ���Ƿ���������Ϣ���û��Զ��嶨λͼ�꣩
		// mCurrentMarker = BitmapDescriptorFactory
		// .fromResource(R.drawable.icon_geo);

		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();// ���ö�λSDK
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������,���ذٶȾ�γ������ϵ coor=bd09ll
		option.setScanSpan(1000);// ���÷���λ����ļ��ʱ��Ϊ1000ms
		option.setNeedDeviceDirect(true);// ���صĶ�λ��������ֻ���ͷ�ķ���
		option.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ
		mLocClient.setLocOption(option);
		mLocClient.start();
		// ���õ�ͼ�����ĵ�
		LatLng cenpt = new LatLng(125.299844, 43.855814);
		// �����ͼ״̬
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(15)//
				.build();
		// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// �ı��ͼ״̬
		mBaiduMap.setMapStatus(mMapStatusUpdate);
		// ���طŴ���С�İ�ť
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			// ���ذٶ�logo ZoomControl
			// if (child instanceof ImageView || child instanceof ZoomControls)
			// {
			if (child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		/*******************************************************************************/
		// �քӶ�λ����ǰλ��
		dingwei = (ImageView) findViewById(R.id.center);
		dingwei.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LatLng ll = new LatLng(latitude, longitude);
				// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		});

		// �քӶ�λ����ǰλ��
		foundlocation = (ImageView) findViewById(R.id.thingslocation);
		foundlocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LatLng ll = new LatLng(latitude1, longitude1);
				// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		});

		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {

					// TODO Auto-generated method stub
					LatLng ll = new LatLng(latitude, longitude);
					// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				} else {
					LatLng ll = new LatLng(latitude1, longitude1);
					// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				}

			}
		});

		/*******************************************************************************/
		// �����ͼĬ�϶�������ͼ��
		// ��ʼ��������
		initMarker();
		// ��Ӹ�����
		addOverlays();
		// ��������ﵯ�������¼�
		init();
	}

	/*******************************************************************************/
	private void initMarker() {
		// TODO Auto-generated method stub
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.mark);
		relativeLayout = (RelativeLayout) findViewById(R.id.item);
	}

	// �ӷ�������ȡֵʱ���ǵ��ø÷���mMarker
	private void addOverlays() {
		mBaiduMap.clear();
		OverlayOptions options;

		/*****************************/
		// �ڹ̶���γ������Ӹ�����
		Marker marker = null;

		LatLng latLng1 = new LatLng(43.855815, 125.299845); // ���û�ѡ���ľ�γ��

		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.mark);
		// ͼ��
		options = new MarkerOptions().position(latLng1).icon(mMarker).zIndex(6);

		marker = (Marker) mBaiduMap.addOverlay(options);
		// ���ݵĴ���
		// Bundle bundle = new Bundle();
		// bundle.putSerializable("info", (Serializable) infos);
		// marker.setExtraInfo(bundle);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng1);
		mBaiduMap.setMapStatus(msu);
	}

	/*******************************************************************************/
	// ��������:������ô������
	private void init() {
		// ��������ﵯ�������¼�
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {

				// Bundle bundle = marker.getExtraInfo();
				// Info info = (Info)bundle.getSerializable("info");

				TextView title = (TextView) relativeLayout
						.findViewById(R.id.title);
				TextView time = (TextView) relativeLayout
						.findViewById(R.id.time);
				TextView lost_time = (TextView) relativeLayout
						.findViewById(R.id.lost_time);
				TextView lost_place = (TextView) relativeLayout
						.findViewById(R.id.lost_place);
				TextView thing_details = (TextView) relativeLayout
						.findViewById(R.id.thing_details);

				lost_place.setText("������");
				relativeLayout.setVisibility(View.VISIBLE);

		
				return true;
			}

		});
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				relativeLayout.setVisibility(View.GONE);
			}

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	/*******************************************************************************/
	/**
	 * ��λSDK��������
	 * 
	 * @param <MapController>
	 * @param <GeoPoint>
	 */
	public class MyLocationListenner implements BDLocationListener {
		// ˵������û��ע���������ʱ���޷�������������
		@Override
		public void onReceiveLocation(BDLocation location) {
			// mapview���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;

			// ���춨λ����
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			// ���ö�λ����
			mBaiduMap.setMyLocationData(locData);
			longitude = location.getLongitude();
			latitude = location.getLatitude();

			// ��һ�ν����ͼ ��λ���ҵ�λ��
			// if (isFirstLoc) {
			// isFirstLoc = false;
			// LatLng ll = new LatLng(location.getLatitude(),
			// location.getLongitude());
			// MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			// mBaiduMap.animateMapStatus(u);
			// }
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/*******************************************************************************/
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
}
