package com.yqq.notificationanalyse;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Window;
import android.widget.RemoteViews;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);//设置为无标题栏
        setContentView(R.layout.activity_main);

        boolean flag = Util.isNotificationEnabled(this);

        if(!flag){
            Util.requestPermission(this);
        }
        //createBaseNotification();
        createMyselfNotification();
        //createHeadsUpNotification();
        //createNoBackStackNotification();
        //createThreeStyleNotification(1);
        //createWithActionNotification();
        //createBackStackNotification();
    }

    public void cancelNotification(){
        //使用cancel和cancelAll取消Notification
    }

    /**
     * 此方法不能确认完成的时间
     */
    private void startDownLoadIndetermineNotification() {
        //第一步,生成一个Notification的构造器，使用support包中的是为了兼容低版本
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //第二步,使用builder设置Notification的参数
        mBuilder.setSmallIcon(R.drawable.paitnbox)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.paitnbox))
                .setContentTitle("Idear")
                .setContentText("xxxxxx")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDeleteIntent(PendingIntent.getActivity(this, 0, null, 0))
                .setAutoCancel(true)////设置点击后自动消失
                .setTicker("来自yqq的祝福")//设置在第一次发出通知时在status bar进行提醒
                .setOnlyAlertOnce(true)//设置没有显示过的Notification只显示声音，和振动
                .setUsesChronometer(true)//会计算Notification显示时间
                .setLights(Color.RED, 0, 1);

        //第三步,声明NotificationManager
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());

        final int mid = 3;
        // Start a lengthy(冗长的) operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr+=5) {
                            // Sets the progress indicator to a max value,100, the current completion
                            // percentage, and "indeterminate" state
                            mBuilder.setProgress(100, incr, true);
                            // Issues the notification
                            mNotificationManager.notify(mid, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(5*1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0,0,false);
                        mNotificationManager.notify(mid, mBuilder.build());
                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start();
    }

    /**
     * 能确认完成的时间
     */
    private void startDownLoadDetermineNotification() {

        //第一步,生成一个Notification的构造器，使用support包中的是为了兼容低版本
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //第二步,使用builder设置Notification的参数
        mBuilder.setSmallIcon(R.drawable.paitnbox)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.paitnbox))
                .setContentTitle("Idear")
                .setContentText("xxxxxx")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDeleteIntent(PendingIntent.getActivity(this, 0, null, 0))
                .setAutoCancel(true)////设置点击后自动消失
                .setTicker("来自yqq的祝福")//设置在第一次发出通知时在status bar进行提醒
                .setOnlyAlertOnce(true)//设置没有显示过的Notification只显示声音，和振动
                .setUsesChronometer(true)//会计算Notification显示时间
                .setLights(Color.RED, 0, 1);

        //第三步,声明NotificationManager
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());

        final int mid = 4;
        // Start a lengthy(冗长的) operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr+=5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                            mNotificationManager.notify(mid, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(5*1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0,0,false);
                        mNotificationManager.notify(mid, mBuilder.build());
                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start();
    }

    private void createBackStackNotification() {
        //如果点击notification后跳转到一个新的Activity，不需要人工创建回退栈
        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Main.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(new Intent(this,Second.class));
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //第一步,生成一个Notification的构造器，使用support包中的是为了兼容低版本
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //第二步,使用builder设置Notification的参数
        mBuilder.setSmallIcon(R.drawable.paitnbox)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.paitnbox))
                .setContentTitle("Idear")
                .setContentText("xxxxxx")
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDeleteIntent(PendingIntent.getActivity(this, 0, null, 0))
                .setAutoCancel(true)////设置点击后自动消失
                .setTicker("来自yqq的祝福")//设置在第一次发出通知时在status bar进行提醒
                .setOnlyAlertOnce(true)//设置没有显示过的Notification只显示声音，和振动
                .setUsesChronometer(true)//会计算Notification显示时间
                .setLights(Color.RED, 0, 1);

        //第三步,声明NotificationManager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());
    }

    private void createWithActionNotification() {
        Intent dismissIntent = new Intent(this, Second.class);
        dismissIntent.setAction("dismiss");
        //FLAG_CANCEL_CURRENT：如果构建的PendingIntent已经存在，则取消前一个，重新构建一个。
        //FLAG_NO_CREATE：如果PendingIntent已经不存在了，将不再构建它。
        //FLAG_ONE_SHOT：表明这里构建的PendingIntent只能使用一次。
        //FLAG_UPDATE_CURRENT：如果构建的PendingIntent已经存在，则替换它，常用。
        PendingIntent pIDismiss = PendingIntent.getActivity(this, 0, dismissIntent, PendingIntent.FLAG_NO_CREATE);

        //第一步,生成一个Notification的构造器，使用support包中的是为了兼容低版本
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //第二步,使用builder设置Notification的参数
        mBuilder.setSmallIcon(R.drawable.paitnbox)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.paitnbox))
                .setContentTitle("Idear")
                .setContentText("xxxxxx")
                .setContentIntent(null)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDeleteIntent(PendingIntent.getActivity(this, 0, null, 0))
                .setAutoCancel(true)////设置点击后自动消失
                .setTicker("来自yqq的祝福")//设置在第一次发出通知时在status bar进行提醒
                .setOnlyAlertOnce(true)//设置没有显示过的Notification只显示声音，和振动
                .setUsesChronometer(true)//会计算Notification显示时间
                .setLights(Color.RED, 0, 1000)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))//或者设置铃声格式为“file:///mnt/sdcard/Xxx.mp3”
                .setVibrate(new long[]{1000,1000})//设置振动间隔
                //.setNumber(2)//在视图右边设置一个数字
                //.setWhen(System.currentTimeMillis())//手动设置通知发生的时间
                .setStyle(new NotificationCompat.BigTextStyle())//标准视图(Normal view)64dp、大视图（Big view）256dp
                .addAction(R.mipmap.ic_launcher,"dismiss",pIDismiss);

        //PendingIntent支出三种特定的意图：启动Activity,启动Service，发生广播，对应方法如下：
        //PendingIntent pendingIntentActivity = PendingIntent.getActivity(context, requestCode, intent, flags);
        //PendingIntent pendingIntentService = PendingIntent.getService(context, requestCode, intent, flags);
        //PendingIntent pendingIntentBroadcast = PendingIntent.getBroadcast(context, requestCode, intent, flags);


        //第三步,声明NotificationManager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());
    }

    /**
     *
     * @param type 代表
     *              0 NotificationCompat.BigPictureStyle, 在细节部分显示一个256dp高度的位图。
     *             1 NotificationCompat.BigTextStyle，在细节部分显示一个大的文本块。
     *             2 NotificationCompat.InboxStyle，在细节部分显示一段行文本。
     */
    private void createThreeStyleNotification(int type) {

        //第一步,生成一个Notification的构造器，使用support包中的是为了兼容低版本
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //第二步,使用builder设置Notification的参数
        mBuilder.setSmallIcon(R.drawable.paitnbox)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.paitnbox))
                .setContentTitle("Idear")
                .setContentText("xxxxxx")
                .setContentIntent(null)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDeleteIntent(PendingIntent.getActivity(this, 0, null, 0))
                .setAutoCancel(true)////设置点击后自动消失
                .setTicker("来自yqq的祝福")//设置在第一次发出通知时在status bar进行提醒
                .setOnlyAlertOnce(true)//设置没有显示过的Notification只显示声音，和振动
                .setUsesChronometer(true)//会计算Notification显示时间
                .setLights(Color.RED, 0, 1);//设置LED的颜色
        //注意bigContentView是在sdk16时引入的，所以需要判断一下。如果小于sdk16则只能定高了。
        //注意bigContentView 的最大高度是100dp
        //这里的实际操作对象是bigContentView,这三个style都是谷歌公司帮用RemoteView我们自定义好的
        switch (type) {
            case 0:
                mBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle("BigPictureStyle")
                        .bigLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.paitnbox)));
                break;
            case 1:

                mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("myNameIsYQQ")//bigText只能设置一次，多次设置会覆盖
                        .setBigContentTitle("YQQTEST")
                        .setSummaryText("Are you ok?"));
                break;
            case 2:
                mBuilder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine("M.Twain (Google+) Haiku is more than a cert...")
                        .addLine("M.Twain Reminder")
                        .addLine("M.Twain Lunch?")
                        .addLine("M.Twain Revised Specs")
                        .addLine("M.Twain ")
                        .addLine("Google Play Celebrate 25 billion apps with Goo..")
                        .addLine("Stack Exchange StackOverflow weekly Newsl...")
                        .setBigContentTitle("6 new message")
                        .setSummaryText("abc@android.com"));
                break;
            default:
                Toast.makeText(this,"没有指定类型",Toast.LENGTH_SHORT).show();
                break;
        }


        //第三步,声明NotificationManager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void createNoBackStackNotification() {
        Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
        resultIntent.putExtra("15118871363", "15118871363");
        //清除回退栈中保存的Activity
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //第一步,生成一个Notification的构造器，使用support包中的是为了兼容低版本
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //第二步,使用builder设置Notification的参数
        mBuilder.setSmallIcon(R.drawable.paitnbox)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.paitnbox))
                .setContentTitle("Idear")
                .setContentText("xxxxxx")
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDeleteIntent(PendingIntent.getActivity(this, 0, null, 0))
                .setAutoCancel(true)////设置点击后自动消失
                .setTicker("来自yqq的祝福")//设置在第一次发出通知时在status bar进行提醒
                .setOnlyAlertOnce(true)//设置没有显示过的Notification只显示声音，和振动
                .setUsesChronometer(true)//会计算Notification显示时间
                .setLights(Color.RED, 0, 1);//设置LED的颜色

        //第三步,声明NotificationManager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void createHeadsUpNotification() {

        //抬头式通知的Intent
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);

        //第一步,生成一个Notification的构造器，使用support包中的是为了兼容低版本
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //第二步,使用builder设置Notification的参数
        mBuilder.setSmallIcon(R.drawable.paitnbox)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.paitnbox))
                .setContentTitle("Idear")
                .setContentText("xxxxxx")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDeleteIntent(PendingIntent.getActivity(this, 0, null, 0))
                .setAutoCancel(true)////设置点击后自动消失
                .setTicker("来自yqq的祝福")//设置在第一次发出通知时在status bar进行提醒
                .setOnlyAlertOnce(true)//设置没有显示过的Notification只显示声音，和振动
                .setUsesChronometer(true)//会计算Notification显示时间
                .setLights(Color.RED, 0, 1)
                .setFullScreenIntent(pendingIntent,true);



        //第三步,声明NotificationManager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());
    }

    /**
     *
     */
    private void createBaseNotification() {

        //第一步,生成一个Notification的构造器，使用support包中的是为了兼容低版本
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //第二步,使用builder设置Notification的参数
        mBuilder.setSmallIcon(R.drawable.paitnbox)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.paitnbox))
                .setContentTitle("Idear")
                .setContentText("xxxxxx")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDeleteIntent(PendingIntent.getActivity(this, 0, null, 0))
                .setAutoCancel(true)////设置点击后自动消失
                .setTicker("来自yqq的祝福")//设置在第一次发出通知时在status bar进行提醒
                .setOnlyAlertOnce(true)//设置没有显示过的Notification只显示声音，和振动
                .setUsesChronometer(true)//会计算Notification显示时间
                .setLights(Color.RED, 0, 1);

        //第三步,声明NotificationManager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());
    }

    /**
     * 自定义的notification
     */
    private void createMyselfNotification() {


        //使用RemoteView创建notification的自定义视图
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.view_expand);


        //第一步,生成一个Notification的构造器，使用support包中的是为了兼容低版本
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //第二步,使用builder设置Notification的参数
        mBuilder.setSmallIcon(R.drawable.paitnbox)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.paitnbox))
                .setContentTitle("Idear")
                .setContentText("xxxxxx")
                .setDefaults(Notification.DEFAULT_ALL)
                .setDeleteIntent(PendingIntent.getActivity(this, 0, null, 0))
                .setContentIntent(PendingIntent.getActivity(this,0,null,0))
                .setAutoCancel(true)//设置点击后自动消失
                .setTicker("来自yqq的祝福")//设置第一次发出通知时在status bar进行提醒
                .setOnlyAlertOnce(true)//设置没有显示过的Notification只显示声音，和振动
                .setUsesChronometer(true)//会计算Notification显示时间
                .setLights(Color.RED, 0, 1);

        Notification notification = mBuilder.build();
        //指定展开时的视图
        if (Build.VERSION.SDK_INT>=24) {
            mBuilder.setCustomBigContentView(remoteViews);

        } else if(Build.VERSION.SDK_INT>=16){
            notification.bigContentView = remoteViews;
        }
        mBuilder.setCustomContentView(remoteViews);
//        //设置通用的抬头式布局
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mBuilder.setCustomHeadsUpContentView(remoteViews);
//        }

        //第三步,声明NotificationManager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, notification);
    }


}
