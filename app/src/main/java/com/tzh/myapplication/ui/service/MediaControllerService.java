package com.tzh.myapplication.ui.service;


import static android.app.Notification.VISIBILITY_PUBLIC;
import static android.telephony.AvailableNetworkInfo.PRIORITY_HIGH;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.RemoteController;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.tzh.myapplication.MainActivity;
import com.tzh.myapplication.R;
import com.tzh.myapplication.utils.Util;

import java.util.List;

public class MediaControllerService extends NotificationListenerService implements RemoteController.OnClientUpdateListener {
    public String MY_TAG = "MediaControllerService";
    public MediaControllerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(MY_TAG, "MediaControllerService onCreate");
        initNotify("MediaController", "MediaControllerService");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        Log.i(MY_TAG, "MediaControllerService onStartCommand");
        if (Util.isNotificationListenerEnabled(this)) {//开启通知使用权后再设置,否则会报权限错误
            initMediaSessionManager();
            registerRemoteController();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(MY_TAG, "MediaControllerService onDestroy");
    }

    //////////////////////////////////MediaController获取音乐信息/////////////////////////////////////
    private List<MediaController> mActiveSessions;
    private MediaController.Callback mSessionCallback;
    private void initMediaSessionManager() {
        MediaSessionManager mediaSessionManager = (MediaSessionManager) getSystemService(MEDIA_SESSION_SERVICE);
        ComponentName localComponentName = new ComponentName(this, MediaControllerService.class);
        mediaSessionManager.addOnActiveSessionsChangedListener(new MediaSessionManager.OnActiveSessionsChangedListener() {
            @Override
            public void onActiveSessionsChanged(@Nullable final List<MediaController> controllers) {
                for (MediaController mediaController : controllers) {
                    String packageName = mediaController.getPackageName();
                    Log.e(MY_TAG, "MyApplication onActiveSessionsChanged mediaController.getPackageName: " + packageName);
                    synchronized (this) {
                        mActiveSessions = controllers;
                        registerSessionCallbacks();
                    }
                }
            }
        }, localComponentName);
        synchronized (this) {
            mActiveSessions = mediaSessionManager.getActiveSessions(localComponentName);
            registerSessionCallbacks();
        }
    }

    private void registerSessionCallbacks() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (MediaController controller : mActiveSessions) {
                if (mSessionCallback == null) {
                    mSessionCallback = new MediaController.Callback() {
                        @Override
                        public void onMetadataChanged(MediaMetadata metadata) {
                            if (metadata != null) {
                                String trackName =
                                        metadata.getString(MediaMetadata.METADATA_KEY_TITLE);
                                String artistName =
                                        metadata.getString(MediaMetadata.METADATA_KEY_ARTIST);
                                String albumArtistName =
                                        metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST);
                                String albumName =
                                        metadata.getString(MediaMetadata.METADATA_KEY_ALBUM);
                                Log.i(MY_TAG, "---------------------------------");
                                Log.i(MY_TAG, "| trackName: " + trackName);
                                Log.i(MY_TAG, "| artistName: " + artistName);
                                Log.i(MY_TAG, "| albumArtistName: " + albumArtistName);
                                Log.i(MY_TAG, "| albumName: " + albumName);
                                Log.i(MY_TAG, "---------------------------------");
                            }
                        }

                        @Override
                        public void onPlaybackStateChanged(PlaybackState state) {
                            if(state != null){
                                boolean isPlaying = state.getState() == PlaybackState.STATE_PLAYING;
                                Log.e(MY_TAG, "MediaController.Callback onPlaybackStateChanged isPlaying: " + isPlaying);
                            }
                        }
                    };
                }
                controller.registerCallback(mSessionCallback);
            }
        }
    }

    //////////////////////////////////RemoteController获取音乐信息/////////////////////////////////////
    public RemoteController remoteController;
    public void registerRemoteController() {
        remoteController = new RemoteController(this, this);
        boolean registered;
        try {
            registered = ((AudioManager) getSystemService(AUDIO_SERVICE)).registerRemoteController(remoteController);
        } catch (NullPointerException e) {
            registered = false;
        }
        if (registered) {
            try {
                remoteController.setArtworkConfiguration(100,100);
                remoteController.setSynchronizationMode(RemoteController.POSITION_SYNCHRONIZATION_CHECK);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClientChange(final boolean clearing) {
        Log.d(MY_TAG, "clearing == " + clearing);
    }

    @Override
    public void onClientPlaybackStateUpdate(final int state) {
        Log.d(MY_TAG, "state1 == " + state);
    }

    @Override
    public void onClientPlaybackStateUpdate(final int state, final long stateChangeTimeMs, final long currentPosMs, final float speed) {
        Log.i(MY_TAG, "state2 == " + state + "stateChangeTimeMs == " + stateChangeTimeMs + "currentPosMs == " + currentPosMs + "speed == " + speed);
    }

    @Override
    public void onClientTransportControlUpdate(final int transportControlFlags) {
        Log.d(MY_TAG, "transportControlFlags == " + transportControlFlags);
    }

    @Override
    public void onClientMetadataUpdate(final RemoteController.MetadataEditor metadataEditor) {
        String artist = metadataEditor.
                getString(MediaMetadataRetriever.METADATA_KEY_ARTIST, "null");
        String album = metadataEditor.
                getString(MediaMetadataRetriever.METADATA_KEY_ALBUM, "null");
        String title = metadataEditor.
                getString(MediaMetadataRetriever.METADATA_KEY_TITLE, "null");
        Long duration = metadataEditor.
                getLong(MediaMetadataRetriever.METADATA_KEY_DURATION, -1);
        Bitmap defaultCover = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_compass);
        Bitmap bitmap = metadataEditor.
                getBitmap(RemoteController.MetadataEditor.BITMAP_KEY_ARTWORK, defaultCover);
        Log.e(MY_TAG, "artist:" + artist + ", album:" + album + ", title:" + title + ", duration:" + duration);
    }

    /**
     * 添加一个常驻通知
     * @param title
     * @param context
     */
    public void initNotify(String title, String context) {
        String CHANNEL_ONE_ID = "1000";

        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher, null);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        //Bitmap bitmapIcon = BitmapUtils.getBitmapFromDrawable(drawable);

        Intent nfIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent;
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfIntent, 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 123, nfIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 123, nfIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        @SuppressLint("WrongConstant") NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), CHANNEL_ONE_ID)
                .setContentIntent(pendingIntent) // 设置PendingIntent
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                //.setLargeIcon(bitmapIcon)// 设置大图标
                .setContentTitle(title)
                .setContentText(context) // 设置上下文内容
                .setWhen(System.currentTimeMillis())// 设置该通知发生的时间
                .setVisibility(VISIBILITY_PUBLIC)// 锁屏显示全部通知
                //.setDefaults(Notification.DEFAULT_ALL)// //使用默认的声音、振动、闪光
                .setPriority(PRIORITY_HIGH);// 通知的优先级

        //----------------  新增代码 ------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //修改安卓8.1以上系统报错
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, "app_service_notify", NotificationManager.IMPORTANCE_MIN);
            notificationChannel.enableLights(false);//如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
            notificationChannel.setShowBadge(false);//是否显示角标
            notificationChannel.enableVibration(false);//是否震动
            notificationChannel.setLockscreenVisibility(VISIBILITY_PUBLIC);//锁屏显示全部通知
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId(CHANNEL_ONE_ID);
        }
        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(1, notification);
    }

}