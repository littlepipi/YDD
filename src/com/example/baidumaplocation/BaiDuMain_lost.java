package com.example.baidumaplocation;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.example.activity.PostLostMessage;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class BaiDuMain_lost extends Activity implements OnGetGeoCoderResultListener   {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	boolean isFirstLoc = true;// 是否首次定位
	// 定位相关
    LocationClient mLocClient;//定位SDK的核心类
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;

	private GeoCoder mSearch ;    // 反地理编码搜索模块，也可去掉地图模块独立使用
	   // 定义搜索服务类  

	private LatLng ll_lost;
    private String mData;  
	private double longitude;//Longitude，Latitude用于手动定位我的位置
	private double latitude;
	private double my_lat_lost;
	private double my_lng_lost;
	private ImageView qidian_lost;//屏幕中心图标
	private ImageView center_lost;//点击获取当前wo所在的位置
	private TextView text;//显示屏幕中心定位的经纬度
	private TextView shezhi_lost;//点击中心图标出现的文本
	public static TextView xuanding_lost;//显示选定的位置	
	private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.center_lost);
        mMapView = (MapView)findViewById(R.id.bmapView_lost);
        mBaiduMap = mMapView.getMap();
        
        mCurrentMode = LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfigeration(
				mCurrentMode, true,mCurrentMarker));
    
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);	
		
	
//		GeoCoder:地理编码查询接口(坐标与位置之间相互转换)
	    mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();//配置定位SDK
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型,返回百度经纬度坐标系 coor=bd09ll 
		option.setScanSpan(1000000000);//设置发起定位请求的间隔时间为1000ms
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		mLocClient.setLocOption(option);
		mLocClient.start();     
		//设置地图的中心点
		LatLng cenpt = new LatLng(125.299844,43.855814); 
	   //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
            .target(cenpt)
            .zoom(15)
            .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate); 
      
       
        
 /*******************************************************************************/
     //自动定位
        center_lost = (ImageView) findViewById(R.id.center_lost);      
        center_lost.setOnClickListener(new OnClickListener(){
   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				LatLng ll = new LatLng(latitude,
   						longitude);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
   			}       	
        });
        
        
      //返回事件
        back = (ImageButton) findViewById(R.id.map_lostback);
        back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BaiDuMain_lost.this,PostLostMessage.class);
				BaiDuMain_lost.this.startActivity(intent);
				BaiDuMain_lost.this.finish();
			}
		});
 /*******************************************************************************/	     
       //屏幕中心按钮跳动事件
        qidian_lost = (ImageView) findViewById(R.id.qidian_lost);
        shezhi_lost = (TextView) findViewById(R.id.dianji_lost);
        text = (TextView) findViewById(R.id.message);
        xuanding_lost= (TextView) findViewById(R.id.message2_lost);
       
        mBaiduMap.setOnMapStatusChangeListener( new BaiduMap.OnMapStatusChangeListener() {
            /**
             * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
             * @param status 地图状态改变开始时的地图状态
             */
            public void onMapStatusChangeStart(MapStatus status){          	
            }
            /**
             * 地图状态变化中
             * @param status 当前地图状态
             */
            public void onMapStatusChange(MapStatus status){
            
            }
            /**
             * 地图状态改变结束
             * @param status 地图状态改变结束后的地图状态
             */
            public void onMapStatusChangeFinish(final MapStatus status){
     	
           	      TranslateAnimation alphaAnimation2 = new TranslateAnimation(0f, 0f, 0f, -40f);
				  alphaAnimation2.setDuration(200);  //设置时间		  			       
			      //为重复执行的次数。如果设置为n，则动画将执行n+1次。Animation.INFINITE为无限制播放
				  alphaAnimation2.setRepeatCount(1); //设置按钮弹跳的次数，也就是快慢	 
//				  alphaAnimation2.setRepeatCount(Animation.INFINITE);
				   
				   //为动画效果的重复模式，常用的取值如下。RESTART：重新从头开始执行。REVERSE：反方向执行
				  alphaAnimation2.setRepeatMode(Animation.REVERSE);					  
				  alphaAnimation2.setInterpolator(new  LinearInterpolator());//动画结束的时候弹起
				  
				  qidian_lost.setAnimation(alphaAnimation2); 
				  alphaAnimation2.start();			  
				  alphaAnimation2.setAnimationListener(new AnimationListener(){

					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						
						shezhi_lost.setVisibility(View.VISIBLE);
						xuanding_lost.setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						
					}					  
				  });
				  
				  //点击设置为丢失物品地点
				  shezhi_lost.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub											  
						Intent intent1 = new Intent(BaiDuMain_lost.this,PostLostMessage.class);						
						BaiDuMain_lost.this.startActivity(intent1);
						BaiDuMain_lost.this.finish();
					}					 					  
				  });	
				  //获取屏幕中心的经纬度
				   ll_lost=status.target;
	               my_lat_lost=ll_lost.latitude;
	               my_lng_lost=ll_lost.longitude;	               				
	               shezhi_lost.setVisibility(View.GONE);	
	               xuanding_lost.setVisibility(View.GONE);
				   SearchButtonProcess();				
				 }	
        });              
    }

    
protected void SearchButtonProcess() {
		// TODO Auto-generated method stub

	  LatLng ptCenter = new LatLng(ll_lost.latitude, ll_lost.longitude);
	  // 反Geo搜索
	  mSearch.reverseGeoCode(new ReverseGeoCodeOption()//发起反地理编码请求(经纬度->地址信息)
			.location(ptCenter));// 要转换的地理坐标	
					 
	}

	/*******************************************************************************/
    /**
     * 定位SDK监听函数
     * @param <MapController>
     * @param <GeoPoint>
     */
    public class MyLocationListenner implements BDLocationListener {//说明：当没有注册监听函数时，无法发起网络请求

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

	        //若是第一次定位：
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}					
/*******************************************************************************/	
		//显示自己具体所在位置
		if (location == null)
			return ;
		StringBuffer sb = new StringBuffer(256);
//		sb.append("\nlatitude : ");
//		sb.append(location.getLatitude());
//		sb.append("\nlontitude : ");
//		sb.append(location.getLongitude());
//		sb.append("\naddr : ");
		sb.append(location.getAddrStr());
		logMsg(sb.toString());
		
		}
							
		public void onReceivePoi(BDLocation poiLocation) {		
			if (poiLocation == null){
				return ; 
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			} 
			logMsg(sb.toString());
		}		
	
		/**
		 * 显示请求字符串
		 * @param str
		 */
		public void logMsg(String str) {
			try {
				mData = str;
				if ( text != null )
					text.setText("您所在的位置："+mData);
			} catch (Exception e) {
				e.printStackTrace();
			}			
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
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		  if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	            //没有找到检索结果  						        		        	
	        }  
	        //获取反向地理编码结果  
	}
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		  if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	            //没有找到检索结果  						        	
	        	Toast.makeText(BaiDuMain_lost.this, result.getAddress(),
	    				Toast.LENGTH_LONG).show(); 
	        }  								
		  	  
		  xuanding_lost.setText(result.getAddress());
		  
		  String lost_place = xuanding_lost.getText().toString();
		  String lost_jingweiduString = ll_lost.toString();
		  
		  //实例化SharedPreferences对象（第一步） 
		  SharedPreferences mySharedPreferences= getSharedPreferences("lost_locate_EditText", 
		  Activity.MODE_PRIVATE); 
		  //实例化SharedPreferences.Editor对象（第二步） 
		  SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		  //用putString的方法保存数据 
		  editor.putString("lost_place", lost_place); 
		  editor.putString("lost_jingweiduString", lost_jingweiduString); 
		  //提交当前数据 
		  editor.commit(); 
		
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        
       Intent intent = new Intent(BaiDuMain_lost.this,PostLostMessage.class);
       
       BaiDuMain_lost.this.startActivity(intent);
       
       finish();
        return true;  
    }   
   
}
