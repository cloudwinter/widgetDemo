package com.summer.network.okhttputil;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 主线程回调结果<br>
 * 
 * Created by xiayundong on 2017/10/20.
 */

public abstract class OKResultHandler<T> implements OKCallback<T> {

	private WeakReference<Context> mContext;

	private static final int MSG_FAILURE = 1;
	private static final int MSG_SUCCESS = 2;

	/**
	 * handler处理ui线程中回调 失败
	 *
	 * @see OKCallback#notifyFailure(int, String)
	 * @param code
	 * @param msg
	 */
	public abstract void onFailure(int code, String msg);

	/**
	 * handler处理ui线程中回调 成功
	 *
	 * @see OKCallback#notifySuccess(Object)
	 * @param data
	 */
	public abstract void onSuccess(T data);

	public OKResultHandler(Context context) {
		mContext = new WeakReference<>(context);
	}

	private Handler mNetHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (hasFinished()) {
				return;
			}
			switch (msg.what) {
			case MSG_FAILURE:
				onFailure(msg.arg1, (String) msg.obj);
				break;
			case MSG_SUCCESS:
				onSuccess((T) msg.obj);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void notifyFailure(int code, String msg) {
		Message message = Message.obtain(mNetHandler, MSG_FAILURE);
		message.arg1 = code;
		message.obj = msg;
		mNetHandler.sendMessage(message);
	}

	@Override
	public void notifySuccess(T data) {
		Message message = Message.obtain(mNetHandler, MSG_SUCCESS);
		message.obj = data;
		mNetHandler.sendMessage(message);
	}

	/**
	 * 当前的activity有没有finish
	 * 
	 * @return
	 */
	private boolean hasFinished() {
		if (mContext == null) {
			return true;
		}
		Context context = mContext.get();
		if (context == null) {
			return true;
		}
		if (context instanceof Activity) {
			Activity activity = (Activity) context;
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
				// 大于17用destroy判断处理
				if (activity.isDestroyed()) {
					return true;
				}
			} else {
				// 否则用finish来判断
				if (activity.isFinishing()) {
					return true;
				}
			}
		} else {
			return false;
		}
		return false;
	}

}
