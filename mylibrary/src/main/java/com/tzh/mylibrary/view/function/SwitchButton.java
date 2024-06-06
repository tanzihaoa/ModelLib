package com.tzh.mylibrary.view.function;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

import com.tzh.mylibrary.R;

public class SwitchButton extends View {
    //选中的背景颜色
    private int mSelectBg;
    //未选中的背景颜色
    private int mUnSelectBg;
    //当前的状态
    private int mState;
    //圆圈与背景的间距
    private int mCircleMargin;
    //画笔
    private Paint mPaint;
    //默认的宽度
    private int mDefaultWidth = 240;
    //圆弧半径
    private int mDefaultRx = 100;
    //打开
    public static final int OPEN = 2;
    //关闭
    public static final int CLOSE = 1;
    private int mCurrstate;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        mSelectBg = ta.getColor(R.styleable.SwitchButton_selectedBg, Color.GREEN);
        mUnSelectBg = ta.getColor(R.styleable.SwitchButton_unselectedBg, Color.GRAY);
        mState = ta.getInt(R.styleable.SwitchButton_switchState, 2);
        mCircleMargin = (int) ta.getDimension(R.styleable.SwitchButton_circle2Bgmargin, 10);
        ta.recycle();
        mCurrstate = mState;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrstate == CLOSE){
                    mCurrstate = OPEN;
                }else {
                    mCurrstate = CLOSE;
                }
                if(mSwitchButtonCall != null){
                    mSwitchButtonCall.check(mCurrstate == OPEN);
                }
                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = mDefaultWidth;
        }

        setMeasuredDimension(width, width / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mCurrstate == CLOSE){
            drawClose(canvas);
        }else {
            drawOpen(canvas);
        }
    }

    private void drawOpen(Canvas canvas) {
        mPaint.setColor(mSelectBg);
        //画背景
        RectF rectF = new RectF(0, 0, getWidth(), getWidth() / 2);
        canvas.drawRoundRect(rectF, mDefaultRx, mDefaultRx, mPaint);
        //画圆
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(getWidth() *3 / 4, getWidth() / 4, getWidth() / 4 - mCircleMargin, mPaint);
    }

    private void drawClose(Canvas canvas) {
        mPaint.setColor(mUnSelectBg);
        //画背景
        RectF rectF = new RectF(0, 0, getWidth(), getWidth() / 2);
        canvas.drawRoundRect(rectF, mDefaultRx, mDefaultRx, mPaint);
        //画圆
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(getWidth() / 4, getWidth() / 4, getWidth() / 4 - mCircleMargin, mPaint);

    }

    /**
     * 设置状态
     */
    public void setState(boolean state){
        if(state){
            mCurrstate = OPEN;
        }else {
            mCurrstate = CLOSE;
        }
        invalidate();
    }

    private SwitchButtonCall mSwitchButtonCall;
    public void setSwitchButtonCall(SwitchButtonCall switchButtonCall){
        mSwitchButtonCall = switchButtonCall;
    }

    public interface SwitchButtonCall {
        void check(boolean isOff);
    }
}
