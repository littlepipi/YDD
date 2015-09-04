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
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	// ��λ���
    LocationClient mLocClient;//��λSDK�ĺ�����
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;

	private GeoCoder mSearch ;    // �������������ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	   // ��������������  

	private LatLng ll_lost;
    private String mData;  
	private double longitude;//Longitude��Latitude�����ֶ���λ�ҵ�λ��
	private double latitude;
	private double my_lat_lost;
	private double my_lng_lost;
	private ImageView qidian_lost;//��Ļ����ͼ��
	private ImageView center_lost;//�����ȡ��ǰwo���ڵ�λ��
	private TextView text;//��ʾ��Ļ���Ķ�λ�ľ�γ��
	private TextView shezhi_lost;//�������ͼ����ֵ��ı�
	public static TextView xuanding_lost;//��ʾѡ����λ��	
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
    
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);	
		
	
//		GeoCoder:��������ѯ�ӿ�(������λ��֮���໥ת��)
	    mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();//���ö�λSDK
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������,���ذٶȾ�γ������ϵ coor=bd09ll 
		option.setScanSpan(1000000000);//���÷���λ����ļ��ʱ��Ϊ1000ms
		option.setNeedDeviceDirect(true);//���صĶ�λ��������ֻ���ͷ�ķ���
		option.setIsNeedAddress(true);//���صĶ�λ���������ַ��Ϣ
		mLocClient.setLocOption(option);
		mLocClient.start();     
		//���õ�ͼ�����ĵ�
		LatLng cenpt = new LatLng(125.299844,43.855814); 
	   //�����ͼ״̬
        MapStatus mMapStatus = new MapStatus.Builder()
            .target(cenpt)
            .zoom(15)
            .build();
        //����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //�ı��ͼ״̬
        mBaiduMap.setMapStatus(mMapStatusUpdate); 
      
       
        
 /*******************************************************************************/
     //�Զ���λ
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
        
        
      //�����¼�
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
       //��Ļ���İ�ť�����¼�
        qidian_lost = (ImageView) findViewById(R.id.qidian_lost);
        shezhi_lost = (TextView) findViewById(R.id.dianji_lost);
        text = (TextView) findViewById(R.id.message);
        xuanding_lost= (TextView) findViewById(R.id.message2_lost);
       
        mBaiduMap.setOnMapStatusChangeListener( new BaiduMap.OnMapStatusChangeListener() {
            /**
             * ���Ʋ�����ͼ�����õ�ͼ״̬�Ȳ������µ�ͼ״̬��ʼ�ı䡣
             * @param status ��ͼ״̬�ı俪ʼʱ�ĵ�ͼ״̬
             */
            public void onMapStatusChangeStart(MapStatus status){          	
            }
            /**
             * ��ͼ״̬�仯��
             * @param status ��ǰ��ͼ״̬
             */
            public void onMapStatusChange(MapStatus status){
            
            }
            /**
             * ��ͼ״̬�ı����
             * @param status ��ͼ״̬�ı������ĵ�ͼ״̬
             */
            public void onMapStatusChangeFinish(final MapStatus status){
     	
           	      TranslateAnimation alphaAnimation2 = new TranslateAnimation(0f, 0f, 0f, -40f);
				  alphaAnimation2.setDuration(200);  //����ʱ��		  			       
			      //Ϊ�ظ�ִ�еĴ������������Ϊn���򶯻���ִ��n+1�Ρ�Animation.INFINITEΪ�����Ʋ���
				  alphaAnimation2.setRepeatCount(1); //���ð�ť�����Ĵ�����Ҳ���ǿ���	 
//				  alphaAnimation2.setRepeatCount(Animation.INFINITE);
				   
				   //Ϊ����Ч�����ظ�ģʽ�����õ�ȡֵ���¡�RESTART�����´�ͷ��ʼִ�С�REVERSE��������ִ��
				  alphaAnimation2.setRepeatMode(Animation.REVERSE);					  
				  alphaAnimation2.setInterpolator(new  LinearInterpolator());//����������ʱ����
				  
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
				  
				  //�������Ϊ��ʧ��Ʒ�ص�
				  shezhi_lost.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub											  
						Intent intent1 = new Intent(BaiDuMain_lost.this,PostLostMessage.class);						
						BaiDuMain_lost.this.startActivity(intent1);
						BaiDuMain_lost.this.finish();
					}					 					  
				  });	
				  //��ȡ��Ļ���ĵľ�γ��
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
	  // ��Geo����
	  mSearch.reverseGeoCode(new ReverseGeoCodeOption()//���𷴵����������(��γ��->��ַ��Ϣ)
			.location(ptCenter));// Ҫת���ĵ�������	
					 
	}

	/*******************************************************************************/
    /**
     * ��λSDK��������
     * @param <MapController>
     * @param <GeoPoint>
     */
    public class MyLocationListenner implements BDLocationListener {//˵������û��ע���������ʱ���޷�������������

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

	        //���ǵ�һ�ζ�λ��
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}					
/*******************************************************************************/	
		//��ʾ�Լ���������λ��
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
		 * ��ʾ�����ַ���
		 * @param str
		 */
		public void logMsg(String str) {
			try {
				mData = str;
				if ( text != null )
					text.setText("�����ڵ�λ�ã�"+mData);
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
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		


		super.onDestroy();
	}
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		  if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	            //û���ҵ��������  						        		        	
	        }  
	        //��ȡ������������  
	}
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		  if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	            //û���ҵ��������  						        	
	        	Toast.makeText(BaiDuMain_lost.this, result.getAddress(),
	    				Toast.LENGTH_LONG).show(); 
	        }  								
		  	  
		  xuanding_lost.setText(result.getAddress());
		  
		  String lost_place = xuanding_lost.getText().toString();
		  String lost_jingweiduString = ll_lost.toString();
		  
		  //ʵ����SharedPreferences���󣨵�һ���� 
		  SharedPreferences mySharedPreferences= getSharedPreferences("lost_locate_EditText", 
		  Activity.MODE_PRIVATE); 
		  //ʵ����SharedPreferences.Editor���󣨵ڶ����� 
		  SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		  //��putString�ķ����������� 
		  editor.putString("lost_place", lost_place); 
		  editor.putString("lost_jingweiduString", lost_jingweiduString); 
		  //�ύ��ǰ���� 
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
