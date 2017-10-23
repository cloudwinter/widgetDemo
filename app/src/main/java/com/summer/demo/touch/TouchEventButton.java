package com.summer.demo.touch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by xiayundong on 2017/8/10.
 */
public class TouchEventButton extends Button {

    private static final String TAG = "TouchEventButton";

    public TouchEventButton(Context context) {
        super(context);
    }

    public TouchEventButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEventButton(Context context, @Nullable AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent" + " motion =" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e(TAG, "dispatchTouchEvent" + " motion =" + event.getAction());
        return super.dispatchTouchEvent(event);
    }

}
