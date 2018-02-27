package test.site.yiyan.weexnativeandroid.weex.download;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chiclaim on 2016/05/18
 */
public class FileDownloadManager {

  private static FileDownloadManager instance;
  private Context context;

  private static final String tmpSuffix = "_tmp";

  private FileDownloadManager(Context context) {
    this.context = context.getApplicationContext();
  }

  public static FileDownloadManager getInstance(Context context) {
    if (instance == null) {
      instance = new FileDownloadManager(context);
    }
    return instance;
  }

  /**
   * 准备下载的位置
   *
   * @return
   */
  public static String prepareDowloadDir(Context context) {
    final File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    if (file == null) {
      throw new IllegalStateException("Failed to get external storage files directory");
    } else if (file.exists()) {
      if (!file.isDirectory()) {
        throw new IllegalStateException(file.getAbsolutePath() +
            " already exists and is not a directory");
      }
    } else {
      if (!file.mkdirs()) {
        throw new IllegalStateException("Unable to create directory: " +
            file.getAbsolutePath());
      }
    }

    return file.getAbsolutePath();
  }

  public void download(String url, final String saveName, final FileDownloadListener listener) {
    Request request = new Request.Builder()
        .url(url)
        .build();
    OkHttpClient mOkHttpClient = new OkHttpClient();
    mOkHttpClient.newCall(request).enqueue(new Callback() {

      private int preProgress = -1;
      private long preTime = -1;

      @Override
      public void onFailure(Call call, IOException e) {
        listener.onFailed(e.getMessage());
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        listener.onStart();
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
          String dir = prepareDowloadDir(context);
          String tmpName = saveName + tmpSuffix;
          File targetFile = new File(dir, saveName);
          if(targetFile.exists()){
            targetFile.delete();
          }
          File tmpFile = new File(dir, tmpName);
          if(tmpFile.exists()){
            tmpFile.delete();
          }
          fos = new FileOutputStream(tmpFile);

          is = response.body().byteStream();
          long total = response.body().contentLength();
          long sum = 0;
          while ((len = is.read(buf)) != -1) {
            fos.write(buf, 0, len);
            sum += len;
            int progress = (int) (sum * 1.0f / total * 100);
            long time = System.currentTimeMillis();
            if (progress != preProgress && time - preTime > 2000L) {
              listener.onUpdate(progress);
              preProgress = progress;
              preTime = time;
            }
          }
          fos.flush();
          tmpFile.renameTo(targetFile);
          String destPath = targetFile.getAbsolutePath();
          listener.onCompleted(destPath);
        } catch (Exception e) {
          listener.onFailed(e.getMessage() + "");
        } finally {
          try {
            if (is != null)
              is.close();
          } catch (IOException e) {
          }
          is = null;
          try {
            if (fos != null)
              fos.close();
          } catch (IOException e) {
          }
          fos = null;
        }
      }

    });
  }

  class DownloadInfo {
    int id;
    String title;
    String serverUrl;
    String localUrl;
    String size;
    String totalSize;
    int status;

  }
}
