package com.summer.demo.decorator;

import android.util.Log;

/**
 * Created by xiayundong on 2017/10/10.
 */

public class Decorator extends Component {

    public static final String TAG = "Decorator";

    private Component mComponent;

    public Decorator(Component component) {
        mComponent = component;
    }

    @Override
    public void operation() {
        super.operation();
        Log.e(TAG, "operation: Decorator operation()");
    }
}
