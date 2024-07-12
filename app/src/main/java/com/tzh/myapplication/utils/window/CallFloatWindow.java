package com.tzh.myapplication.utils.window;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tzh.myapplication.R;
import com.tzh.myapplication.base.MyApplication;
import com.tzh.myapplication.ui.activity.CallActivity;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class CallFloatWindow {
    private static final String TAG = "EaseCallFloatWindow";

    private static CallFloatWindow instance;

    private WindowManager windowManager = null;
    private WindowManager.LayoutParams layoutParams = null;
    private SurfaceView surfaceView;

    private View floatView;
    private TextView tvContent;
    private int screenWidth;
    private int floatViewWidth;
    private int callType;  //  0语音  1视频
    private int uId;
    private long costSeconds;
    private ConferenceInfo conferenceInfo;
    private SingleCallInfo singleCallInfo;
    // 计时器
    private long mNow; // the currently displayed time
    private long mBase;
    private boolean callState = true; //是否连接成功通话
    private StringBuilder mRecycle = new StringBuilder("MM:SS");
    Timer timer ;

    public CallFloatWindow(Context context) {
        initFloatWindow(context);
    }

    private CallFloatWindow() {
    }


    public static CallFloatWindow getInstance(Context context) {
        if (instance == null) {
            instance = new CallFloatWindow(context);
        }
        return instance;
    }

    public static CallFloatWindow getInstance() {
        if (instance == null) {
            synchronized (CallFloatWindow.class) {
                if (instance == null) {
                    instance = new CallFloatWindow();
                }
            }
        }
        return instance;
    }

    private void initFloatWindow(Context context) {
        windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
    }

    private MyChronometer chronometer;

    public void setCostSeconds(long seconds) {
        this.costSeconds = seconds;
    }

    /**
     * add float window
     */
    public void show() { // 0: voice call; 1: video call;
        if (floatView != null) {
            return;
        }
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.END | Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.type = getSupportedWindowType();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        //显示的位置
        layoutParams.y = 300;
        floatView = LayoutInflater.from(MyApplication.mContext).inflate(R.layout.activity_float_window, null);
        tvContent = (TextView) floatView.findViewById(R.id.tv_content);
        floatView.setFocusableInTouchMode(true);

        if (floatView instanceof ViewGroup) {
            chronometer = new MyChronometer(MyApplication.mContext);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150, 150);
            ((ViewGroup) floatView).addView(chronometer, params);
        }

        windowManager.addView(floatView, layoutParams);
        startCount();
        if (callType == 1) {
            conferenceInfo = new ConferenceInfo();
        } else {
            singleCallInfo = new SingleCallInfo();
        }
        floatView.post(new Runnable() {
            @Override
            public void run() {
                // Get the size of floatView;
                if (floatView != null) {
                    floatViewWidth = floatView.getWidth();
                }
            }
        });

        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class<? extends Activity> callClass = CallActivity.class;
                Log.e("TAG", "current call class: "+callClass);
                if(callClass != null) {
                    Intent intent = new Intent(MyApplication.mContext, callClass);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//                    if(callType != EaseCallType.CONFERENCE_CALL) {
//                        intent.putExtra("uId", singleCallInfo != null ? singleCallInfo.remoteUid : 0);
//                    }
//                    intent.putExtra("isClickByFloat", true);
//                    EaseCallKit.getInstance().getAppContext().startActivity(intent);
                    MyApplication.mContext.startActivity(intent);
                }else {
                    Log.e(TAG, "Current call class is null, please not call EaseCallKit.getInstance().releaseCall() before the call is finished");
                }
                //dismiss();
            }
        });

        floatView.setOnTouchListener(new View.OnTouchListener() {
            boolean result = false;

            int left;
            int top;
            float startX = 0;
            float startY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        result = false;
                        startX = event.getRawX();
                        startY = event.getRawY();

                        left = layoutParams.x;
                        top = layoutParams.y;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(event.getRawX() - startX) > 20 || Math.abs(event.getRawY() - startY) > 20) {
                            result = true;
                        }

                        int deltaX = (int) (startX - event.getRawX());

                        layoutParams.x = left + deltaX;
                        layoutParams.y = (int) (top + event.getRawY() - startY);
                        windowManager.updateViewLayout(floatView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        smoothScrollToBorder();
                        break;
                }
                return result;
            }
        });
        initTimeObserver();
    }

    public int getSupportedWindowType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            return WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
    }

    private void startCount() {
        if (chronometer != null) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }
    }

    private void stopCount() {
        if (chronometer != null) {
            chronometer.stop();
        }
    }

    /**
     * Should call the method before call {@link #dismiss()}
     *
     * @return Cost seconds in float window
     */
    public long getFloatCostSeconds() {
        if (chronometer != null) {
            return chronometer.getCostSeconds();
        }
        Log.e(TAG, "chronometer is null, can not get cost seconds");
        return 0;
    }

    /**
     * Should call the method before call {@link #dismiss()}
     *
     * @return Total cost seconds
     */
    public long getTotalCostSeconds() {
        if (chronometer != null) {
            Log.e("activity", "costSeconds: " + chronometer.getCostSeconds());
        }
        if (chronometer != null) {
            return costSeconds + chronometer.getCostSeconds();
        }
        Log.e(TAG, "chronometer is null, can not get total cost seconds");
        return 0;
    }

    public void setConferenceInfo(ConferenceInfo info) {
        this.conferenceInfo = info;
    }

    public ConferenceInfo getConferenceInfo() {
        return conferenceInfo;
    }

    /**
     * Update conference call state

     */
    public void update() {
        if (floatView == null) {
            return;
        }
        // uId = memberView.getUserId();
        if (true) { // 视频未开启
            floatView.findViewById(R.id.layout_call_voice).setVisibility(View.VISIBLE);
            floatView.findViewById(R.id.layout_call_video).setVisibility(View.GONE);
        } else { // 视频已开启
            floatView.findViewById(R.id.layout_call_voice).setVisibility(View.GONE);
            floatView.findViewById(R.id.layout_call_video).setVisibility(View.VISIBLE);

//            int uId = memberView.getUserId();
//            boolean isSelf = TextUtils.equals(userAccount, EMClient.getInstance().getCurrentUser());
            prepareSurfaceView(false, uId);
        }
    }

    /**
     * Update the sing call state
     *
     * @param isSelf
     * @param curUid
     * @param remoteUid
     * @param surface
     */
    public void update(boolean isSelf, int curUid, int remoteUid, boolean surface) {
        if (singleCallInfo == null) {
            singleCallInfo = new SingleCallInfo();
        }
        singleCallInfo.curUid = curUid;
        singleCallInfo.remoteUid = remoteUid;
        if (callType == 1 && surface) {
            floatView.findViewById(R.id.layout_call_voice).setVisibility(View.GONE);
            floatView.findViewById(R.id.layout_call_video).setVisibility(View.VISIBLE);
            prepareSurfaceView(isSelf, isSelf ? curUid : remoteUid);
        } else {
            floatView.findViewById(R.id.layout_call_voice).setVisibility(View.VISIBLE);
            floatView.findViewById(R.id.layout_call_video).setVisibility(View.GONE);
        }
    }

    public void setContent(String text) {
        if (tvContent != null) {
            tvContent.post(new Runnable() {
                @Override
                public void run() {
                    tvContent.setText(text);
                }
            });
        }
    }

    public SingleCallInfo getSingleCallInfo() {
        return singleCallInfo;
    }

    public void setCameraDirection(boolean isFront, boolean changeFlag) {
        if (singleCallInfo == null) {
            singleCallInfo = new SingleCallInfo();
        }
        singleCallInfo.isCameraFront = isFront;
        singleCallInfo.changeFlag = changeFlag;
    }

    public boolean isShowing() {
        if (callType == 1) {
            return true;
        } else {
            return floatView != null;
        }
    }

    /**
     * For the single call, only the remote uid is returned
     *
     * @return
     */
    public int getUid() {
      /*  if(callType == EaseCallType.CONFERENCE_CALL && memberView != null) {
            return memberView.getUserId();
        }else if((callType == EaseCallType.SINGLE_VIDEO_CALL || callType == EaseCallType.SINGLE_VOICE_CALL) && singleCallInfo != null) {
            return singleCallInfo.remoteUid;
        }*/
        return -1;
    }

    /**
     * 停止悬浮窗
     */
    public void dismiss() {
        Log.i(TAG, "dismiss: ");
        if (windowManager != null && floatView != null) {
            stopCount();
            windowManager.removeView(floatView);
        }
        cancel();
        floatView = null;
        surfaceView = null;
        if (conferenceInfo != null) {
            conferenceInfo = null;
        }
        if (singleCallInfo != null) {
            singleCallInfo = null;
        }
    }

    /**
     * 设置视频
     */
    private void prepareSurfaceView(boolean isSelf, int uid) {
        RelativeLayout surfaceLayout = (RelativeLayout) floatView.findViewById(R.id.layout_call_video);
        surfaceLayout.removeAllViews();
        // surfaceView =pocEngine.getRendererView(IPocEngineEventHandler.SurfaceType.LOCAL_SURFACE_TEXTURE);
        surfaceLayout.addView(surfaceView);
        surfaceView.setZOrderOnTop(false);
        surfaceView.setZOrderMediaOverlay(false);
       /* if(isSelf){
            rtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN,0));
        }else{
            rtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        }*/
    }

    private void smoothScrollToBorder() {
        Log.i(TAG, "screenWidth: " + screenWidth + ", floatViewWidth: " + floatViewWidth);
        int splitLine = screenWidth / 2 - floatViewWidth / 2;
        final int left = layoutParams.x;
        final int top = layoutParams.y;
        int targetX;

        if (left < splitLine) {
            // 滑动到最左边
            targetX = 0;
        } else {
            // 滑动到最右边
            targetX = screenWidth - floatViewWidth;
        }

        ValueAnimator animator = ValueAnimator.ofInt(left, targetX);
        animator.setDuration(100)
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (floatView == null) return;

                        int value = (int) animation.getAnimatedValue();
                        Log.i(TAG, "onAnimationUpdate, value: " + value);
                        layoutParams.x = value;
                        layoutParams.y = top;
                        windowManager.updateViewLayout(floatView, layoutParams);
                    }
                });
        animator.start();
    }

    public static class SingleCallInfo {
        /**
         * Current user's uid
         */
        public int curUid;
        /**
         * The other size of uid
         */
        public int remoteUid;
        /**
         * Camera direction: front or back
         */
        public boolean isCameraFront = true;
        /**
         * A tag used to mark the switch between local and remote video
         */
        public boolean changeFlag;
    }
    /**
     * Use to hold the conference info
     */
    public static class ConferenceInfo {
        public Map<Integer, ViewState> uidToViewList;
        public Map<String, Integer> userAccountToUidMap;
        //  public Map<Integer, EaseUserAccount> uidToUserAccountMap;
        public static class ViewState {
            // video state
            public boolean isVideoOff;
            // audio state
            public boolean isAudioOff;
            // screen mode
            public boolean isFullScreenMode;
            // speak activate state
            public boolean speakActivated;
            // camera direction
            public boolean isCameraFront;
        }
    }
    public long getmBase() {
        return mBase;
    }
    public void setmBase(long mBase) {
        this.mBase = mBase;
    }
    public boolean isCallState() {
        return callState;
    }
    public void setCallState(boolean callState) {
        this.callState = callState;
    }
    public void initTimeObserver() {
        timer = new Timer();
        //双重保证通话中
        if (callState) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updateText(SystemClock.elapsedRealtime());
                }
            }, 0, 1000);
           /* Observable.timer(1000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread())
                    .doOnSubscribe(disposable -> disposableObserver=disposable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            updateText(SystemClock.elapsedRealtime());
                        }
                    });*/
        } else {
            setContent("等待接听");
        }
    }
    public void cancel() {
        //timer cancel后不能再次调用schedule方法，需要重新创建，所以可以调用task.cancel方法取消任务
        //timer.cancel();
        if (timer != null) {
            timer.cancel();
        }
    }
    private synchronized void updateText(long now) {
        mNow = now;
        long seconds = now - mBase;
        seconds /= 1000;
        boolean negative = false;
        if (seconds < 0) {
            seconds = -seconds;
            negative = true;
        }
        String text = DateUtils.formatElapsedTime(mRecycle, seconds);
        setContent(text);
    }
}