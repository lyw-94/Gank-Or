package com.sdust.gankor.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Liu Yongwei on 2016/10/30.
 * <p>
 * version : 1.0
 */

public class CommonUtils {

    /**
     * 关闭流
     * @param closeable
     */
    public static void close(Closeable closeable) {

        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
