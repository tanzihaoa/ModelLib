package com.tzh.mylibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.tzh.mylibrary.R;

public class RatioImageView extends AppCompatImageView {

    /**
     * 宽高比例
     */
    private float mRatio = 0f;

    private int type = 2;

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);

        mRatio = typedArray.getFloat(R.styleable.RatioImageView_ratio, 0f);

        type = typedArray.getInt(R.styleable.RatioImageView_WidthOrHeight, 2);
        typedArray.recycle();
    }

    public RatioImageView(Context context) {
        super(context);
    }

    /**
     * 设置ImageView的宽高比
     *
     * @param ratio 倍数
     */
    public void setRatio(float ratio) {
        mRatio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(type == 1){
            int height = MeasureSpec.getSize(heightMeasureSpec);
            if (mRatio != 0) {
                float width = height * mRatio;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) width, MeasureSpec.EXACTLY);
            }
        }else{
            int width = MeasureSpec.getSize(widthMeasureSpec);
            if (mRatio != 0) {
                float height = width * mRatio;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
