
package cn.core.mobile.library.tools;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * SD card 操作
 * 
 * @author Richard.Ma
 */
public class SDCardUtils {

    /**
     * 检测sdcard是否可用
     * 
     * @return <code>true</code>为可用，否则为不可用
     */
    public static boolean isAvailable() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED))
            return false;
        return true;
    }

    /**
     * 获取可用空间大小。
     * 
     * @return
     */
    public static long getAvailableSize() {
        long size = 0;
        if (isAvailable()) {
            File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();

            size = blockSize * availableBlocks;
        }
        return size;
    }

    /**
     * 判断sdcard上是否有足够的空间。
     * 
     * @param updateSize 需要空间大小。
     * @return 如果有足够空间就返回<code>true</code>，否则返回<code>false</code>
     *         。（sdcard没有挂载也返回<code>false</code>）
     */
    public static boolean enoughSpace(long updateSize) {
        if (isAvailable()) {
            return (updateSize < getAvailableSize());
        }
        return false;
    }
}
