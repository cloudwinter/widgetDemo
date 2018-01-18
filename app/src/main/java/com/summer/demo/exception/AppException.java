package com.summer.demo.exception;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author zhangxiuliang
 * @description app崩溃捕捉异常日志并弹提示框（仅供测试）
 */
public class AppException implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static AppException instance;
    private UncaughtExceptionHandler mDefaultHandler;
    private FileLogger mFileLogger;
    private Context mContext;

    private AppException(Context context) {
        init(context);
    }

    public static AppException getInstance(Context context) {
        if (instance == null) {
            instance = new AppException(context);
        }
        Toast.makeText(context, "已启动异常捕获程序", Toast.LENGTH_SHORT).show();
        return instance;
    }

    private void init(Context context) {
    	mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    private void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        final String msg = sw.toString();
        writeInFile(msg);
        startThrowableTip(msg);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            android.util.Log.e(TAG, "error : " + e);
        }
        exit();
        return true;
    }
    
    private void writeInFile(String msg) {
    	mFileLogger = new FileLogger("exitlog", "appexit");
        mFileLogger.println(msg);
        Logger.log().e(msg);
	}

	private void startThrowableTip(final String msg){
    	new Thread() {   
        	@Override   
        	public void run() {   
        		Looper.prepare();  
        		String errorCause = msg.length() > 50 ? msg.substring(0, 50) : msg;
        		String fileName = null != mFileLogger ? mFileLogger.getFileName() : "";
        		showErrorDialog("[奔溃原因]:\n{"+errorCause+"...}\n[奔溃日志存放位置]：\n{"
        				+ fileName+"}\n\n---请将奔溃日志提取交于开发---");
        		Looper.loop();   
        	}   
        }.start();   
    }
    
    private void showErrorDialog(String error){
    	AlertDialog alert = new AlertDialog.Builder(mContext).setTitle("程序奔溃啦...")
    			.setMessage(error)
    			.setPositiveButton("4秒后自动关闭,点我关闭",new DialogInterface.OnClickListener() { 
    				
    				@Override  
    				public void onClick(DialogInterface dialog, int which) {
    					dialog.dismiss();
    					exit();
    				}  
    			}).create();
    	Window window = alert.getWindow();
    	window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    	alert.show();
    }
}