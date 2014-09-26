
package cn.core.mobile.library.tools;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

/**
 * 应用缓存文件操作类
 * 
 * @author Will.Wu </br> Create at 2014年2月19日 下午5:05:41
 * @version 1.0
 */
public class CacheUtils {
    public static final String          TAG                = CacheUtils.class.getSimpleName();
    private static Md5FileNameGenerator sFileNameGenerator = new Md5FileNameGenerator();

    /**
     * 根据<code>fileUrl</code>取得需要缓存文件名。
     * 
     * @param fileUrl 需要缓存的文件url.
     * @return
     */
    public static String getCacheFilename(String fileUrl) {
        return sFileNameGenerator.generate(fileUrl);
    }

    /**
     * 检查缓存目录是否存在目标文件
     * 
     * @param fileUrl
     * @return
     */
    public static boolean fileIsExist(Context context, String fileUrl) {
        String filePath = getCacheFilePath(context, fileUrl);
        // 检查SD卡缓存目录是否存在该文件
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 获取缓存文件的绝对路径路径.
     * 
     * @param context 上下文.
     * @param fileUrl 需要缓存的文件url.
     * @return
     */
    public static String getCacheFilePath(Context context, String fileUrl) {
        return getCacheDirectory(context).getAbsoluteFile() + File.separator
                + getCacheFilename(fileUrl);
    }

    /**
     * 得到缓存目录。
     * 
     * @param context 上下文.
     * @return
     */
    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            String msg = "Can't define system cache directory!" + cacheDirPath + " will be used.";
            LogUtil.w(TAG, msg);
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    /**
     * 删除文件缓存文件。
     * 
     * @param context 上下文
     * @param fileUrl 缓存的文件url.
     * @return 删除成功返回<code>true</code>, 否则返回<code>false</code>
     * @author Will.Wu
     */
    public static boolean deleteCacheFile(Context context, String fileUrl) {
        String filePath = getCacheFilePath(context, fileUrl);
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * 清楚缓存。
     * 
     * @param context 上下文
     * @author Will.Wu
     */
    public static void clearCache(Context context) {
        File[] files = getCacheDirectory(context).listFiles();
        if (files != null) {
            for (File f : files)
                f.delete();
        }
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"),
                "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                LogUtil.w(TAG, "Unable to create external cache directory");
                return null;
            }

            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                LogUtil.w(TAG,
                        "Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

}
