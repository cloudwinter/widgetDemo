package com.summer.demo.decorator;

import android.util.Log;

/**
 * Created by xiayundong on 2017/10/10.
 */

public class ConcreateComponent extends Component {

    public static final String TAG = "ConcreateComponent";

    @Override
    public void operation() {
        super.operation();
        Log.e(TAG, "operation: ConcreateComponent operation()");
    }
}
