
package com.drill.imagechooser.api;

import android.os.Environment;

import java.io.File;

public class FileUtils {
    /**
     * Returns the path of the folder specified in external storage
     * @param foldername
     * @return
     */
    public static String getDirectory(String foldername) {
        File directory = null;
        directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + foldername);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory.getAbsolutePath();
    }

    public static String getFileExtension(String filename) {
        String extension = "";
        try {
            extension = filename.substring(filename.lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extension;
    }

}
