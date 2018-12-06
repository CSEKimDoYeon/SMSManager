package com.example.kimdoyeon.smsmanager.AppWidget;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.kimdoyeon.smsmanager.ImportantMessagesActivity;
import com.example.kimdoyeon.smsmanager.R;

import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {


    private static final String TAG = "HelloWidgetProvider";
    private static final int WIDGET_UPDATE_INTERVAL = 5000;
    private static PendingIntent mSender;
    private static AlarmManager mManager;

    public String testString = "zzzz";

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //Date now = new Date();
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        updateViews.setTextViewText(R.id.tv_widget, testString);
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        // 위젯 업데이트 인텐트를 수신했을 때
        if(action.equals("android.appwidget.action.APPWIDGET_UPDATE"))
        {
            Log.w(TAG, "android.appwidget.action.APPWIDGET_UPDATE");


            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

            SharedPreferences sharedPreferences = context.getSharedPreferences(ImportantMessagesActivity.STATE,
                    Activity.MODE_PRIVATE);

            String result = sharedPreferences.getString(ImportantMessagesActivity.STATE, "hahaha");
            remoteViews.setTextViewText(R.id.tv_widget, result);
            Log.d("sharedPreferences", result);

            //업데이트
            ComponentName thisWidget = new ComponentName(context, NewAppWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(thisWidget, remoteViews);


            //testString = intent.getExtras().getString("key");
            //Log.w(TAG, testString);

            /*removePreviousAlarm();
            long firstTime = System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL;
            mSender = PendingIntent.getBroadcast(context, 0, intent, 0);
            mManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mManager.set(AlarmManager.RTC, firstTime, mSender);*/
        }
        // 위젯 제거 인텐트를 수신했을 때
        else if(action.equals("android.appwidget.action.APPWIDGET_DISABLED"))
        {
            Log.w(TAG, "android.appwidget.action.APPWIDGET_DISABLED");
            removePreviousAlarm();
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them


        super.onUpdate(context, appWidgetManager, appWidgetIds);


        for ( int appWidgetId : appWidgetIds){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget );

            Intent intent = new Intent(context, ImportantMessagesActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            this.refresh(context,remoteViews); // 새로고침작업
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews); // 새로고침 완료 휴 위젯에게 업데이트 명령

        }


        /*// 현재 클래스로 등록된 모든 위젯의 리스트를 가져옴
        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;
        for(int i = 0 ; i < N ; i++)
        {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);

            Toast.makeText(context, "onUpdate(): [" + String.valueOf(i) + "] " + String.valueOf(appWidgetId), Toast.LENGTH_SHORT).show();
        }*/
    }

    private void refresh(Context context, RemoteViews remoteViews){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ImportantMessagesActivity.STATE,
                Activity.MODE_PRIVATE);

        String result = sharedPreferences.getString(ImportantMessagesActivity.STATE, "hahaha");
        remoteViews.setTextViewText(R.id.tv_widget, result);
        Log.d("sharedPreferences", result);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void removePreviousAlarm()
    {
        if(mManager != null && mSender != null)
        {
            mSender.cancel();
            mManager.cancel(mSender);
        }
    }

}

