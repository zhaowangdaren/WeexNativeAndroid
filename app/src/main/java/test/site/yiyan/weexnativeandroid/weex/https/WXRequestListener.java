package test.site.yiyan.weexnativeandroid.weex.https;

/**
 * Created by gtja on 2018/2/10.
 */

public interface WXRequestListener {
  void onSuccess(WXHttpTask task);

  void onError(WXHttpTask task);
}
