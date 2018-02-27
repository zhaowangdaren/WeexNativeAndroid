package test.site.yiyan.weexnativeandroid.weex.download;

/**
 * Created by gtja on 2018/2/11.
 */

public interface FileDownloadListener {
  void onStart();

  void onUpdate(int progress);

  void onCompleted(String filePath);

  void onFailed(String reason);
}
