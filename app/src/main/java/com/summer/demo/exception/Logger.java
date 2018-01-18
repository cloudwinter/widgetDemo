package com.summer.demo.exception;


import java.util.Hashtable;
import android.util.Log;

public class Logger
{  
    public static boolean                	logFlag         = true;  
      
    public static String                  tag         ;
    private final static int                    logLevel        = Log.VERBOSE;  
    private static Hashtable<String, Logger>  sLoggerTable    = new Hashtable<String, Logger>();  
    private String                              mClassName;  
    private FileLogger                          mFileLogger;
    private static final String 				COMMON 		= "@";

    private Logger(String className, String logFileName) {
        mClassName = className;
        tag = ExceptionLogUtil.sApp.getPackageName();
        String filename = StringUtil.trimToEmpty(logFileName);
        if (StringUtil.isNotBlank(filename)){
            mFileLogger = new FileLogger("Log", filename);
        }
    }

    /** 
     * @param className 
     * @return 
     */  
    private static Logger getLogger(String className, String logFileName)
    {  
        Logger classLogger = (Logger) sLoggerTable.get(className);  
        if(classLogger == null)  
        {  
            classLogger = new Logger(className,logFileName);
            sLoggerTable.put(className, classLogger);  
        }  
        return classLogger;  
    }  
      
    /** 
     * Purpose:Mark user two 
     * @return 
     */  
    public static Logger log()  
    {  
        return getLogger(COMMON, StringUtil.EMPTY);
    }
    
    /** 
     * Purpose:Mark by tag
     * @return 
     */  
    public static Logger logByTag(String tag)  
    {  
        return getLogger(tag, StringUtil.EMPTY);
    }

    /**
     * 获得一个带写入到文件的Logger
     * 生成的Log文件路径为/sdcard/android/data/包/cache/log/tag×××
     * @param tag Log的tag 及 文件名前缀，
     * @return
     */
    public static Logger log2File(String tag)
    {
        return getLogger(tag, tag);
    }
    /** 
     * Get The Current Function Name 
     * @return 
     */  
    private String getFunctionName()  
    {  
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();  
        if(sts == null)  
        {  
            return null;  
        }  
        for(StackTraceElement st : sts)  
        {  
            if(st.isNativeMethod())  
            {  
                continue;  
            }  
            if(st.getClassName().equals(Thread.class.getName()))  
            {  
                continue;  
            }  
            if(st.getClassName().equals(this.getClass().getName()))  
            {  
                continue;  
            }  
            return mClassName + "[ " + Thread.currentThread().getName() + ": "  
                    + st.getFileName() + ":" + st.getLineNumber() + " "  
                    + st.getMethodName() + " ]";  
        }  
        return null;  
    }  
    
    private String getStackTrace()  
    {  
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();  
        if(sts == null)  
        {  
            return null;  
        }  
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement st : sts)  
        {  
            if(st.isNativeMethod())  
            {  
                continue;  
            }  
            if(st.getClassName().equals(Thread.class.getName()))  
            {  
                continue;  
            }  
            if(st.getClassName().equals(this.getClass().getName()))  
            {  
                continue;  
            }  
            sb.append(mClassName + "[ " + Thread.currentThread().getName() + ": "  
                    + st.getFileName() + ":" + st.getLineNumber() + " "  
                    + st.getMethodName() + " ]\n");  
        }  
        return sb.toString();  
    }   

    public void printStackTrace(Object str)  
    {  
        if(logFlag)  
        {  
            if(logLevel <= Log.INFO)  
            {  
                String name = getStackTrace();  
                if(name != null)  
                {  
                    Log.i(tag, str + "\n" + name);  
                }  
                else  
                {  
                    Log.i(tag, str.toString());  
                }
            }
        }  
          
    }  
    
    /** 
     * The Log Level:i 
     * @param str 
     */  
    public void i(Object str)  
    {  
        if(logFlag)  
        {  
            if(logLevel <= Log.INFO)  
            {  
                String name = getFunctionName();  
                if(name != null)  
                {  
                    Log.i(tag, name + " - " + str);  
                }  
                else  
                {  
                    Log.i(tag, str.toString());  
                }
                if (mFileLogger != null){
                    mFileLogger.println("[i] "+str.toString());
                }
            }  
        }  
          
    }  
      
    /** 
     * The Log Level:d 
     * @param str 
     */  
    public void d(Object str)  
    {  
        if(logFlag)  
        {  
            if(logLevel <= Log.DEBUG)  
            {  
                String name = getFunctionName();  
                if(name != null)  
                {  
                    Log.d(tag, name + " - " + str);  
                }  
                else  
                {  
                    Log.d(tag, str.toString());  
                }
                if (mFileLogger != null){
                    mFileLogger.println("[d] "+str.toString());
                }
            }  
        }  
    }  
      
    /** 
     * The Log Level:V 
     * @param str 
     */  
    public void v(Object str)  
    {  
        if(logFlag)  
        {  
            if(logLevel <= Log.VERBOSE)  
            {  
                String name = getFunctionName();  
                if(name != null)  
                {  
                    Log.v(tag, name + " - " + str);  
                }  
                else  
                {  
                    Log.v(tag, str.toString());  
                }
                if (mFileLogger != null){
                    mFileLogger.println("[v] "+str.toString());
                }
            }  
        }  
    }  
      
    /** 
     * The Log Level:w 
     * @param str 
     */  
    public void w(Object str)  
    {  
        if(logFlag)  
        {  
            if(logLevel <= Log.WARN)  
            {  
                String name = getFunctionName();  
                if(name != null)  
                {  
                    Log.w(tag, name + " - " + str);  
                }  
                else  
                {  
                    Log.w(tag, str.toString());  
                }
                if (mFileLogger != null){
                    mFileLogger.println("[w] "+str.toString());
                }
            }  
        }  
    }  
      
    /** 
     * The Log Level:e 
     * @param str 
     */  
    public void e(Object str)  
    {  
        if(logFlag)  
        {  
            if(logLevel <= Log.ERROR)  
            {  
                String name = getFunctionName();  
                if(name != null)  
                {  
                    Log.e(tag, name + " - " + str);  
                }  
                else  
                {  
                    Log.e(tag, str.toString());  
                }
                if (mFileLogger != null){
                    mFileLogger.println("[e] "+str.toString());
                }
            }  
        }  
    }  
      
    /** 
     * The Log Level:e 
     * @param ex 
     */  
    public void e(Throwable ex)  
    {  
        if(logFlag)  
        {  
            if(logLevel <= Log.ERROR)  
            {  
                Log.e(tag, "error", ex);  
            }  
        }  
    }  
      
    /** 
     * The Log Level:e 
     * @param log 
     * @param tr 
     */  
    public void e(String log, Throwable tr)  
    {  
        if(logFlag)  
        {  
            String line = getFunctionName();  
            Log.e(tag, "{Thread:" + Thread.currentThread().getName() + "}"  
                    + "[" + mClassName + line + ":] " + log + "\n", tr);
            if (mFileLogger != null){
                mFileLogger.println("[E] "+log);
            }
        }  
    }  
}  
