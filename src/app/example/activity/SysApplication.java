package app.example.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/*将下面SysApplication这个类复制到工程里面，
然后在每个Acitivity的oncreate方法里面通过SysApplication.getInstance().addActivity(this);
添加当前Acitivity到ancivitylist里面去，
最后在想退出的时候调用SysApplication.getInstance().exit();
可直接关闭所有的Acitivity并退出应用程序。*/
 
//该类主要用于关闭整个程序退出时，不能正常关闭的actually，


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