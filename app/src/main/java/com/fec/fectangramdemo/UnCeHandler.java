package com.fec.fectangramdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by XQ Yang on 2017/10/19  11:02.
 * Description :
 */

public class UnCeHandler implements Thread.UncaughtExceptionHandler {
    private Context mContext;
    private static UnCeHandler INSTANCE = new UnCeHandler();
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
    private ExecutorService mPoolExecutor;
    private UnCeHandler() {
    }

    public static UnCeHandler getINSTANCE() {
        return INSTANCE;
    }

    public  void init(Context context) {
        mContext = context;
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        if(!handleException(ex) && mDefaultUncaughtExceptionHandler != null){
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultUncaughtExceptionHandler.uncaughtException(t, ex);
        }else{
            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            //            Intent intent = new Intent(mContext, SplashActivity.class);
            //            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //            PendingIntent restartIntent = PendingIntent.getActivity(
            //                    mContext, 0, intent,
            //                    PendingIntent.FLAG_ONE_SHOT);
            //
            //            //退出程序
            //
            //
            //            AlarmManager mgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            //            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
            //                    restartIntent); // 1秒钟后重启应用


            //杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            //彻底杀死当前app
            ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(mContext.getPackageName());
            System.gc();
            //            application.finishAllActivity();
        }
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        if (mPoolExecutor == null) {
            mPoolExecutor = Executors.newCachedThreadPool();
        }
        //使用Toast来显示异常信息
        mPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出现异常,即将退出.\r\n我们将在第一时间进行修复，不便之处敬请谅解",
                    Toast.LENGTH_SHORT).show();
                Looper.loop();
                //CrashReport.postCatchedException(ex);
            }
        });
        ex.printStackTrace();
        return true;
    }
}
