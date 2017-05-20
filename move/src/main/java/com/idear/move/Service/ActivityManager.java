package com.idear.move.Service;

import android.app.Activity;

import java.util.Arrays;
import java.util.LinkedList;

public class ActivityManager {

		//用来结束Activity的服务类
	
	    private LinkedList<Activity> activityLinkedList = new LinkedList<Activity>();
	    
	    private ActivityManager() {
	    }
	    
	    private static ActivityManager instance;
	    
	    public static ActivityManager getInstance(){
	        if(null == instance){
	            instance = new ActivityManager();
	        }
	        return instance;
	    }
	    
	    //向list中添加Activity
	    public ActivityManager addActivity(Activity activity){
	        activityLinkedList.add(activity);
	        return instance;
	    }
	    
	    //结束特定的Activity(s)
	    public ActivityManager finishActivities(Class<? extends Activity>... activityClasses){
	        for (Activity activity : activityLinkedList) {
	            if( Arrays.asList(activityClasses).contains( activity.getClass() ) ){
	                activity.finish();
	            }
	        }
	        return instance;
	    }
	    
	    //结束所有的Activities
	    public ActivityManager finishAllActivities() {
	        for (Activity activity : activityLinkedList) {
	            activity.finish();
	        }
	        return instance;
	    }
}
