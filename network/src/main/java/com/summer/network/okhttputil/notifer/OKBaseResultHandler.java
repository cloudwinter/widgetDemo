package com.summer.network.okhttputil.notifer;

import java.lang.ref.WeakReference;

import com.summer.network.code.NetCode;
import com.summer.network.okhttputil.entity.OKResult;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

/**
 * 主线程回调结果<br>
 * 
 * Created by xiayundong on 2017/10/20.
 */

public abstract class OKBaseResultHandler<MessageType, DataType>
		implements OKResultNotifier<OKResult<MessageType, DataType>> {

	private WeakReference<Context> mContext;

	private static final int MSG_FAILURE = 1;
	private static final int MSG_SUCCESS = 2;

	/**
	 * handler处理UI线程中回调 错误异常
	 * 
	 * @param code
	 * @param msg
	 */
	public abstract void onFailure(int code, MessageType msg);

	/**
	 * handler处理UI线程中回调，成功回调
	 * 
	 * @param data
	 */
	public abstract void onSuccess(DataType data);

	/**
	 * 子类实现协助本地异常转化为对应的msgType类型
	 * 
	 * @param msg
	 * @return
	 */
	public abstract MessageType getErrorMsg(String msg);

	public OKBaseResultHandler(Context context) {
		mContext = new WeakReference<>(context);
	}

	@Override
	public void notifyFailure(int code, String msg) {
		MessageType messageType = getErrorMsg(msg);
		Message message = Message.obtain(mNetHandler, MSG_FAILURE);
		message.obj = messageType;
		mNetHandler.sendMessage(message);
	}

	@Override
	public void notifySuccess(OKResult<MessageType, DataType> data) {
		if (data == null) {
			notifyFailure(NetCode.RESULT_CODE_RESPONSE_NULL,
					NetCode.RESULT_CODE_RESPONSE_NULL_MSG);
			return;
		}
		Message message;
		if (data.resultCode == NetCode.RESULT_CODE_SUCCESS) {
			message = Message.obtain(mNetHandler, MSG_SUCCESS);
			message.obj = data.data;
		} else {
			message = Message.obtain(mNetHandler, MSG_FAILURE);
			message.obj = data.resultMsg;
		}
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

	private Handler mNetHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (hasFinished()) {
				return;
			}
			switch (msg.what) {
			case MSG_FAILURE:
				onFailure(msg.arg1, (MessageType) msg.obj);
				break;
			case MSG_SUCCESS:
				onSuccess((DataType) msg.obj);
				break;
			default:
				break;
			}
		}
	};

}
