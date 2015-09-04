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
	boolean isFirstLoc = true;// 是否首次定位
	// 定位相关
	LocationClient mLocClient;// 定位SDK的核心类
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	BDLocation location;

	private double longitude;// Longitude，Latitude用于手动定位我的位置
	private double latitude;
	private Button shi;// 丢东西的按钮
	private ImageView dingwei, foundlocation;// 点击获取当前定位坐标，我的位置按钮，点击获取我现在所在的位置
	// 覆盖物相关
	private BitmapDescriptor mMarker;
	// 编辑框布局,初始化时隐藏该布局
	private RelativeLayout relativeLayout;

	// 物品的经纬度
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

		// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
		// mCurrentMarker = BitmapDescriptorFactory
		// .fromResource(R.drawable.icon_geo);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();// 配置定位SDK
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型,返回百度经纬度坐标系 coor=bd09ll
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		mLocClient.setLocOption(option);
		mLocClient.start();
		// 设置地图的中心点
		LatLng cenpt = new LatLng(125.299844, 43.855814);
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(15)//
				.build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
		// 隐藏放大缩小的按钮
		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			// 隐藏百度logo ZoomControl
			// if (child instanceof ImageView || child instanceof ZoomControls)
			// {
			if (child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		/*******************************************************************************/
		// 手動定位到當前位置
		dingwei = (ImageView) findViewById(R.id.center);
		dingwei.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LatLng ll = new LatLng(latitude, longitude);
				// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		});

		// 手動定位到當前位置
		foundlocation = (ImageView) findViewById(R.id.thingslocation);
		foundlocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LatLng ll = new LatLng(latitude1, longitude1);
				// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
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
					// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				} else {
					LatLng ll = new LatLng(latitude1, longitude1);
					// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				}

			}
		});

		/*******************************************************************************/
		// 进入地图默认丢东西的图标
		// 初始化覆盖物
		initMarker();
		// 添加覆盖物
		addOverlays();
		// 点击覆盖物弹出窗口事件
		init();
	}

	/*******************************************************************************/
	private void initMarker() {
		// TODO Auto-generated method stub
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.mark);
		relativeLayout = (RelativeLayout) findViewById(R.id.item);
	}

	// 从服务器获取值时就是调用该方法mMarker
	private void addOverlays() {
		mBaiduMap.clear();
		OverlayOptions options;

		/*****************************/
		// 在固定经纬度上添加覆盖物
		Marker marker = null;

		LatLng latLng1 = new LatLng(43.855815, 125.299845); // 是用户选定的经纬度

		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.mark);
		// 图标
		options = new MarkerOptions().position(latLng1).icon(mMarker).zIndex(6);

		marker = (Marker) mBaiduMap.addOverlay(options);
		// 数据的传输
		// Bundle bundle = new Bundle();
		// bundle.putSerializable("info", (Serializable) infos);
		// marker.setExtraInfo(bundle);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng1);
		mBaiduMap.setMapStatus(msu);
	}

	/*******************************************************************************/
	// 问题所在:数据怎么传过来
	private void init() {
		// 点击覆盖物弹出窗口事件
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

				lost_place.setText("覃宏艺");
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
	 * 定位SDK监听函数
	 * 
	 * @param <MapController>
	 * @param <GeoPoint>
	 */
	public class MyLocationListenner implements BDLocationListener {
		// 说明：当没有注册监听函数时，无法发起网络请求
		@Override
		public void onReceiveLocation(BDLocation location) {
			// mapview销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;

			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			// 设置定位数据
			mBaiduMap.setMyLocationData(locData);
			longitude = location.getLongitude();
			latitude = location.getLatitude();

			// 第一次进入地图 定位到我的位置
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
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
}
