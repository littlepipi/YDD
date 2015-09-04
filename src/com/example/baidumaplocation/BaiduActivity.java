//package com.example.baidumaplocation;
//
//import java.io.Serializable;
//import java.util.List;
//
//import net.loonggg.fragment.R;
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.ZoomControls;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
//import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapPoi;
//import com.baidu.mapapi.map.MapStatus;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.MyLocationConfigeration;
//import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//public class BaiduActivity extends Activity {
//	private MapView mMapView;
//	private BaiduMap mBaiduMap;
//	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
//	// ��λ���
//    LocationClient mLocClient;//��λSDK�ĺ�����
//	public MyLocationListenner myListener = new MyLocationListenner();
//	private LocationMode mCurrentMode;
//	private BitmapDescriptor mCurrentMarker;
//	BDLocation location;
//		
//	private double longitude;//Longitude��Latitude�����ֶ���λ�ҵ�λ��
//	private double latitude;
//	private Button shi ;//�������İ�ť
//	private Button zhao;//�Ҷ����İ�ť
//	private ImageView dingwei;//�����ȡ��ǰ��λ���꣬�ҵ�λ�ð�ť�������ȡ���������ڵ�λ��
//	
//	
//	//���������
//	private BitmapDescriptor mMarker;
//	private BitmapDescriptor mMarker2;
//	
//	//�༭�򲼾�,��ʼ��ʱ���ظò���
//	private RelativeLayout relativeLayout;
//
//		
//	
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        SDKInitializer.initialize(getApplicationContext());
//        setContentView(R.layout.baidu_activity_main);
//        mMapView = (MapView)findViewById(R.id.bmapView);
//        mBaiduMap = mMapView.getMap();
// 
//        mCurrentMode = LocationMode.NORMAL;
//        mBaiduMap.setMyLocationConfigeration(new MyLocationConfigeration(
//				mCurrentMode, true,mCurrentMarker));
//        
//          // ���ö�λͼ������ã���λģʽ���Ƿ���������Ϣ���û��Զ��嶨λͼ�꣩  
////        mCurrentMarker = BitmapDescriptorFactory  
////            .fromResource(R.drawable.icon_geo); 
//        
//		// ������λͼ��
//		mBaiduMap.setMyLocationEnabled(true);	
//		// ��λ��ʼ��
//		mLocClient = new LocationClient(this);	
//		mLocClient.registerLocationListener(myListener);
//		LocationClientOption option = new LocationClientOption();//���ö�λSDK
//		option.setOpenGps(true);// ��gps
//		option.setCoorType("bd09ll"); // ������������,���ذٶȾ�γ������ϵ coor=bd09ll 
//		option.setScanSpan(1000);//���÷���λ����ļ��ʱ��Ϊ1000ms
//		option.setNeedDeviceDirect(true);//���صĶ�λ��������ֻ���ͷ�ķ���
//		option.setIsNeedAddress(true);//���صĶ�λ���������ַ��Ϣ
//		mLocClient.setLocOption(option);
//		mLocClient.start();     
//		//���õ�ͼ�����ĵ�
//		LatLng cenpt = new LatLng(125.299844,43.855814); 
//	   //�����ͼ״̬
//        MapStatus mMapStatus = new MapStatus.Builder()
//            .target(cenpt)
//            .zoom(15)//
//            .build();
//        //����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
//        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//        //�ı��ͼ״̬
//        mBaiduMap.setMapStatus(mMapStatusUpdate);     
//        //���طŴ���С�İ�ť
//        int count = mMapView.getChildCount();
//        for (int i = 0; i < count; i++) {        	
//            View child = mMapView.getChildAt(i);
//            // ���ذٶ�logo ZoomControl
//            // if (child instanceof ImageView || child instanceof ZoomControls)
//            // {
//            if (child instanceof ZoomControls) {            	
//                child.setVisibility(View.INVISIBLE);                
//            }
//        }    
///*******************************************************************************/  		        
//        //�քӶ�λ����ǰλ��
//        dingwei = (ImageView) findViewById(R.id.center);
//        dingwei.setOnClickListener(new OnClickListener(){
//   			@Override
//   			public void onClick(View arg0) {
//   				// TODO Auto-generated method stub
//   				LatLng ll = new LatLng(latitude,
//   						longitude);
//   			   //����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//				mBaiduMap.animateMapStatus(u);
//   			}       	
//        });       
//      
///*******************************************************************************/      
//        //������ 
//        shi = (Button) findViewById(R.id.putong);
//        shi.setOnClickListener(new OnClickListener(){
//
//   			@Override
//   			public void onClick(View arg0) {		
//   		      //��ʼ��������
//   		        initMarker();
//   		      //��Ӹ�����
//   		    	addOverlays(Info.info);   
//   		      //����������¼�
//   		       init();
//   			}       	
//        });    
/////*******************************************************************************/  		          
//////        �Ҷ��� 
////        zhao = (Button) findViewById(R.id.weixing);
////        zhao.setOnClickListener(new OnClickListener(){
////   			@Override
////   			public void onClick(View arg0) {
////   		      //��ʼ��������
////   		          initMarker2();
////   		      //��Ӹ�����
////   		      	addOverlays2(Info2.info2);
////   		     //����������¼�
////   		      init2();
////   		      }		
////   			}); 	
///*******************************************************************************/             
//        //�����ͼĬ�϶�������ͼ��
//		  //��ʼ��������
//		  initMarker();
//		  //��Ӹ�����
//		  addOverlays(Info.info); 
//		  //��������ﵯ�������¼�
//	      init();	
//	}
///*******************************************************************************/ 
//    
//    private void initMarker() {
//		// TODO Auto-generated method stub  	
//    	mMarker = BitmapDescriptorFactory.fromResource(R.drawable.ico_to_there); 
//    	relativeLayout= (RelativeLayout) findViewById(R.id.item);
// 	}
//	
//        //�ӷ�������ȡֵʱ���ǵ��ø÷���mMarker
//    private void addOverlays(List<Info> info) {    	
//    	mBaiduMap.clear();
//    	LatLng latLng = null ;
//    	Marker marker = null;
//    	OverlayOptions options;   	
//    	//ͨ��forѭ���ڵ�ͼ����ӱ�־��
//    	for (Info infos:info) {
//			//��γ��
//    		latLng = new LatLng(infos.getLatitude(), infos.getLongitude());
//    		//ͼ��
//    		options = new MarkerOptions()
//    		.position(latLng)
//    		.icon(mMarker)
//    		.zIndex(5);
//    		marker = (Marker)mBaiduMap.addOverlay(options);    		
//    		Bundle bundle = new Bundle();
//    		bundle.putSerializable("info", (Serializable) infos);    		
//    		marker.setExtraInfo(bundle);	 
//		}    	
//    	MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
//    	mBaiduMap.setMapStatus(msu);  	
//	}
///*******************************************************************************/     
//    
//       //��������:������ô������
//        private void init() {
//    	        //��������ﵯ�������¼�	 			    	  
//                mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {   						
//	                 @Override
//                     public boolean onMarkerClick(Marker marker) {
//	                	
//	     
//	                	  
//	                	 
//	                	 
//	                        Bundle bundle = marker.getExtraInfo();
//	                        Info info = (Info)bundle.getSerializable("info");	
//
//	                        TextView title = (TextView) relativeLayout.findViewById(R.id.title);
//	                        TextView time = (TextView) relativeLayout.findViewById(R.id.time);
//	                        TextView lost_time = (TextView) relativeLayout.findViewById(R.id.lost_time);
//	                        TextView lost_place = (TextView) relativeLayout.findViewById(R.id.lost_place);
//	                        TextView thing_details = (TextView) relativeLayout.findViewById(R.id.thing_details);
////	                        Button button=(Button)relativeLayout.findViewById(R.id.check_details); 
//	                        
//	                        
//	                        lost_place.setText(info.getName());
//	                        relativeLayout.setVisibility(View.VISIBLE);
//	
//	               //����鿴���鰴ť�¼�         
////	               button.setOnClickListener(new OnClickListener(){
////
////		                @Override
////		                public void onClick(View arg0) {
////		                	Intent i = new Intent(BaiduActivity.this,null);
////		                	BaiduActivity.this.startActivity(i);
////	                		BaiduActivity.this.finish();
////		                        }    		
////	                           });
//	                 return true;
//		       }
//	      });	
//                  mBaiduMap.setOnMapClickListener(new OnMapClickListener(){
//                     @Override
//                     public void onMapClick(LatLng arg0) {
//		                relativeLayout.setVisibility(View.GONE);   					  
//                       }
//        @Override
//        public boolean onMapPoiClick(MapPoi arg0) {
//                  	// TODO Auto-generated method stub		
//	                  return false;
//                    }   					 
//                  });              
//        }
//           
//          
///*******************************************************************************/    		  
//	
//    private void initMarker2() { 	
//    	mMarker2 = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);   
//    	relativeLayout= (RelativeLayout) findViewById(R.id.item);
//	}
//    private void addOverlays2(List<Info2> info2) {   	
//    	mBaiduMap.clear();
//    	LatLng latLng = null ;
//    	Marker marker = null;
//    	OverlayOptions options;   	
//    	//ͨ��forѭ���ڵ�ͼ����ӱ�־��
//    	for (Info2 info:info2) {
//			//��γ��
//    		latLng = new LatLng(info.getLatitude(), info.getLongitude());
//    		//ͼ��
//    		options = new MarkerOptions()
//    		.position(latLng)
//    		.icon(mMarker2)
//    		.zIndex(5);
//    		marker = (Marker)mBaiduMap.addOverlay(options);
//    		
//    		Bundle bundle = new Bundle();
//    		bundle.putSerializable("info", (Serializable) info);
//    		
//    		marker.setExtraInfo(bundle);	
//		}
//    	MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
//    	mBaiduMap.setMapStatus(msu); 	
//	}
//    
//    private void init2() {		
//        //��������ﵯ�������¼�	 			    	  
//	    mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {   						
//		@Override
//	    public boolean onMarkerClick(Marker marker) {
//				// TODO Auto-generated method stu				
//	    //���ݴ���
//		Bundle bundle = marker.getExtraInfo();
//		Info2 info = (Info2)bundle.getSerializable("info");	
//				
//		   
//        TextView title = (TextView) relativeLayout.findViewById(R.id.title);
//        TextView time = (TextView) relativeLayout.findViewById(R.id.time);
//        TextView lost_time = (TextView) relativeLayout.findViewById(R.id.lost_time);
//        TextView lost_place = (TextView) relativeLayout.findViewById(R.id.lost_place);
//        TextView thing_details = (TextView) relativeLayout.findViewById(R.id.thing_details);
////        Button button=(Button)relativeLayout.findViewById(R.id.check_details); 
//        
//        
//        lost_place.setText(info.getName());
//		
////		wupin.setText("������");	
//		relativeLayout.setVisibility(View.VISIBLE);
//	
//		//������鰴ť�¼�
////		button.setOnClickListener(new OnClickListener(){
////			@Override
////			public void onClick(View arg0) {
////				Intent i = new Intent(BaiduActivity.this,null);
////				BaiduActivity.this.startActivity(i);
////				BaiduActivity.this.finish();
////			}    		
////		});
//		return true;
//	}
//});	
//	mBaiduMap.setOnMapClickListener(new OnMapClickListener(){
//	@Override
//	public void onMapClick(LatLng arg0) {
//			relativeLayout.setVisibility(View.GONE);   					  
//	}
//	@Override
//	public boolean onMapPoiClick(MapPoi arg0) {
//		// TODO Auto-generated method stub		
//		  return false;
//	}   					 
//});	
//}
//    
//    
//    
//    
//	/*******************************************************************************/
//	/**
//     * ��λSDK��������
//     * @param <MapController>
//     * @param <GeoPoint>
//     */
//    public class MyLocationListenner implements BDLocationListener {
//    	//˵������û��ע���������ʱ���޷�������������
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			// mapview���ٺ��ڴ����½��յ�λ��
//			if (location == null || mMapView == null)
//				return;
//			
//			// ���춨λ����  
//			 MyLocationData locData = new MyLocationData.Builder()
//				.accuracy(location.getRadius())
//				// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
//				.direction(100).latitude(location.getLatitude())
//				.longitude(location.getLongitude()).build();
//			 
//			// ���ö�λ���� 
//		    mBaiduMap.setMyLocationData(locData);		    
//		    longitude = location.getLongitude();
//			latitude = location.getLatitude();	
//
//			if (isFirstLoc) {
//				isFirstLoc = false;
//				LatLng ll = new LatLng(location.getLatitude(),
//						location.getLongitude());
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//				mBaiduMap.animateMapStatus(u);
//			}		
//		}
//		public void onReceivePoi(BDLocation poiLocation) {}
//}    
///*******************************************************************************/	
//    @Override
//	protected void onPause() {
//		mMapView.onPause();
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		mMapView.onResume();
//		super.onResume();
//	}
//
//	@Override
//	protected void onDestroy() {
//		// �˳�ʱ���ٶ�λ
//		mLocClient.stop();
//		// �رն�λͼ��
//		mBaiduMap.setMyLocationEnabled(false);
//		mMapView.onDestroy();
//		mMapView = null;
//		super.onDestroy();
//	}  
//}
