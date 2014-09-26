
package cn.core.mobile.library;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class BaseApplicition extends Application {
    private boolean mFirstLaunch  = true;
    private String  mVersionName = "";
    private int     mVersionCode = 1;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // 设置优先级
                .denyCacheImageMultipleSizesInMemory()
                // 禁止在内存中缓存同一张图片的多个尺寸
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);

        PackageInfo pi = getPackageInfo();
        if (pi != null) {
            mVersionCode = pi.versionCode;
            mVersionName = pi.versionName;
        }
    }

    /**
     * 返回该应用是否是第一次进入
     * 
     * @return
     * @author Will.Wu
     */
    public boolean isFirstLaunch() {
        return mFirstLaunch;
    }

    /**
     * 设置进入状态。（要在启动activity里调用）
     * 
     * @param val
     * @author Will.Wu
     */
    public void setFirstLaunch(boolean val) {
        mFirstLaunch = val;
    }

    public PackageInfo getPackageInfo() {
        PackageManager pm = getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(getApplicationInfo().packageName, 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pi;
    }

    public String getVersionName() {
        return mVersionName;
    }

    public int getVersionCode() {
        return mVersionCode;
    }

}
