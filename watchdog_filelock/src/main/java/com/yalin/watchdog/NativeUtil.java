package com.yalin.watchdog;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static com.jinyalin.log.LogUtil.D;
import static com.jinyalin.log.LogUtil.isNativeLogEnabled;


/**
 * 作者：YaLin
 * 日期：2016/4/23.
 */
public class NativeUtil {
    private static final String TAG = "NativeUtil";

    private static final String PREFIX_NAME = "file_lock";

    static final String assets_filename = "watchdog_file_lock";
    static final String private_filename = "libwatchdog";
    static final String exe_filename = "watchdog";

    private static String createUniqueName() {
        return PREFIX_NAME + SystemClock.uptimeMillis() + ".dat";
    }

    static void clearUnusedFileLocks(Context context, String exceptName) {
        File[] files = context.getFilesDir().getParentFile().listFiles();
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.startsWith(PREFIX_NAME) && !fileName.equals(exceptName)) {
                file.delete();
            }
        }
    }

    private static String getAbiFolder() {
        String abi;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abi = Build.SUPPORTED_ABIS[0];
        } else {
            //noinspection deprecation
            abi = Build.CPU_ABI;
        }
        String folder = null;
        if (abi.contains("armeabi-v7a")) {
            folder = "armeabi-v7a";
        } else if (abi.contains("x86_64")) {
            folder = "x86_64";
        } else if (abi.contains("x86")) {
            folder = "x86";
        } else if (abi.contains("armeabi")) {
            folder = "armeabi";
        }
        return folder;
    }

    public static String runExecutable(Context context, String service, String action, int interval) {
        if (!writeFile(context)) {
            return "Run executable failed. Abi not support.";
        }
        String alias = exe_filename;

        String fileLockName = createUniqueName();

        String outputNativeLog = isNativeLogEnabled() ? "1" : "0";
        String args = service + " " + action + " " + fileLockName + " " + interval
                + " " + outputNativeLog;

        String path = context.getFilesDir().getParentFile().getAbsolutePath();
        String src = path + File.separator + "files" + File.separator + private_filename;
        String des = path + File.separator + alias;
        String cmdRun = path + File.separator + alias + " " + args;
        String cmdChmod = "chmod 777 " + des;
        String cmdCopy = "dd if=" + src + " of=" + des;
        StringBuffer sbResult = new StringBuffer();

        runLocalUserCommand(path, cmdCopy, sbResult);
        runLocalUserCommand(path, cmdChmod, sbResult);
        runLocalUserCommand(path, cmdRun, sbResult);

        clearUnusedFileLocks(context, fileLockName);
        return sbResult.toString();
    }

    public static boolean runLocalUserCommand(String path, String command,
                                              StringBuffer sbOutResult) {
        Process process;
        try {
            process = Runtime.getRuntime().exec("sh");
            DataInputStream inputStream = new DataInputStream(process.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());

            outputStream.writeBytes("cd " + path + "\n");
            outputStream.writeBytes(command + " &\n");
            outputStream.writeBytes("exit\n");
            outputStream.flush();

            process.waitFor();

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String s = new String(buffer);
            if (sbOutResult != null && s.length() != 0) {
                sbOutResult.append("CMD Result:\n").append(s);
            }
        } catch (Exception e) {
            if (sbOutResult != null) {
                sbOutResult.append("Exception:").append(e.getMessage());
            }
            return false;
        }
        return true;
    }

    private static boolean writeFile(Context context) {
        String abiFolder = getAbiFolder();
        if (TextUtils.isEmpty(abiFolder)) {
            return false;
        }
        D(TAG, "Matched abi is " + abiFolder);
        String path = context.getFilesDir().getAbsolutePath();
        String src = path + File.separator + private_filename;

        File file = new File(src);
        if (!file.exists()) {
            InputStream inputStream;
            try {
                inputStream = context.getAssets()
                        .open(abiFolder + File.separator + assets_filename);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedOutputStream bufferedOutputStream =
                        new BufferedOutputStream(new FileOutputStream(file));
                int fileLen = bufferedInputStream.available();
                ByteBuffer byteBuffer = ByteBuffer.allocate(fileLen);
                byte[] buffer = new byte[4096];
                int len;
                while ((len = bufferedInputStream.read(buffer)) > 0) {
                    byteBuffer.put(buffer, 0, len);
                }
                bufferedOutputStream.write(byteBuffer.array());
                bufferedInputStream.close();
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File already exist.");
        }
        return true;
    }
}
