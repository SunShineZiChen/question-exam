package com.example.mylibrary.paper.viewpage;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class MScroller extends Scroller {
    private int mDuration = 300;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    public MScroller(Context context) {
        this(context, sInterpolator);
    }

    public MScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setMDuration(int time) {
        mDuration = time;
    }

    public int getMDuration() {
        return mDuration;
    }
}

