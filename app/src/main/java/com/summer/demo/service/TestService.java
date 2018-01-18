package com.summer.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by xiayundong on 2017/12/8.
 */

public class TestService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
