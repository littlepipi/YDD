package app.example.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/*������SysApplication����ิ�Ƶ��������棬
Ȼ����ÿ��Acitivity��oncreate��������ͨ��SysApplication.getInstance().addActivity(this);
��ӵ�ǰAcitivity��ancivitylist����ȥ��
��������˳���ʱ�����SysApplication.getInstance().exit();
��ֱ�ӹر����е�Acitivity���˳�Ӧ�ó���*/
 
//������Ҫ���ڹر����������˳�ʱ�����������رյ�actually��


public class SysApplication extends Application { 
    private List<Activity> mList = new LinkedList(); 
    private static SysApplication instance; 
 
    private SysApplication() {   
    } 
    public synchronized static SysApplication getInstance() { 
        if (null == instance) { 
            instance = new SysApplication(); 
        } 
        return instance; 
    } 
    // add Activity  
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    } 
 
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }  
}