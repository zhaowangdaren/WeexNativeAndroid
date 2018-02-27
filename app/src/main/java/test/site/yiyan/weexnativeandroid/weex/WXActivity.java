package test.site.yiyan.weexnativeandroid.weex;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import com.taobao.weex.common.WXRenderStrategy;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import test.site.yiyan.weexnativeandroid.R;
import test.site.yiyan.weexnativeandroid.weex.https.HotRefreshManager;
import test.site.yiyan.weexnativeandroid.weex.https.WXHttpManager;
import test.site.yiyan.weexnativeandroid.weex.https.WXHttpTask;
import test.site.yiyan.weexnativeandroid.weex.https.WXRequestListener;

public class WXActivity extends AppCompatActivity implements IWXRenderListener, Handler.Callback, IActivityNavBarSetter{

  private static final String TAG = "WXActivity";
  private WXSDKInstance mInstance;
  private Uri mUri;
  private Handler mWXHandler;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wx);
    WXSDKEngine.setActivityNavBarSetter(this);
    mInstance = new WXSDKInstance(this);
    mInstance.registerRenderListener(this);

    initUIAndData();
    Uri tempUri = getIntent().getData();
    Log.i("tempUri", tempUri.toString());
    mUri = getIntent().getData();
    loadWXfromService(mUri.toString());
    startHotRefresh();
  }

  private void initUIAndData() {
    mWXHandler = new Handler(this);
    HotRefreshManager.getInstance().setHandler(mWXHandler);
  }
  private void startHotRefresh() {
    try{
      URL host = new URL(mUri.toString());
      String wsUrl = "ws://" + host.getHost() + ":" + host.getPort() + "/sockjs-node/websocket";
      if (host.getPort() < 0) {
        wsUrl = "ws://" + host.getHost() + "/sockjs-node/websocket";
      }
      Toast.makeText(this, wsUrl, Toast.LENGTH_SHORT).show();
      mWXHandler.obtainMessage(Constants.HOT_REFRESH_CONNECT, 0, 0, wsUrl).sendToTarget();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }
  private void loadWXfromService(final String url) {
    Log.i("loadWXfromService", url);
//    RenderContainer renderContainer = new RenderContainer(this);
//    mContainer.addView(renderContainer);

    mInstance = new WXSDKInstance(this);
//    mInstance.setRenderContainer(renderContainer);
    mInstance.registerRenderListener(this);
    mInstance.setBundleUrl(url);
    mInstance.setTrackComponent(true);

    WXHttpTask httpTask = new WXHttpTask();
    httpTask.url = url;
    httpTask.requestListener = new WXRequestListener() {

      @Override
      public void onSuccess(WXHttpTask task) {
        Log.i(TAG, "into--[http:onSuccess] url:" + url);
        //          mConfigMap.put("bundleUrl", url);
        mInstance.renderByUrl(url.substring(url.lastIndexOf(File.pathSeparator)), url, null, null, WXRenderStrategy.APPEND_ASYNC);
//          mInstance.render(TAG, new String(task.response.data, "utf-8"), mConfigMap, null, ScreenUtil.getDisplayWidth(WXPageActivity.this), ScreenUtil.getDisplayHeight(WXPageActivity.this), WXRenderStrategy.APPEND_ASYNC);
      }

      @Override
      public void onError(WXHttpTask task) {
        Log.i(TAG, "into--[http:onError]");
//        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "network error!", Toast.LENGTH_SHORT).show();
      }
    };

    WXHttpManager.getInstance().sendRequest(httpTask);
  }
  @Override
  public void onViewCreated(WXSDKInstance instance, View view) {
    setContentView(view);
  }

  @Override
  public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

  }

  @Override
  public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

  }

  @Override
  public void onException(WXSDKInstance instance, String errCode, String msg) {

  }

  @Override
  protected  void onResume() {
    super.onResume();
    if (mInstance != null) {
      mInstance.onActivityResume();
    }
  }

  @Override
  protected  void onPause() {
    super.onPause();
    if (mInstance != null) {
      mInstance.onActivityPause();
    }
  }

  @Override
  protected  void onStop() {
    super.onStop();
    if (mInstance != null) {
      mInstance.onActivityStop();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if(mInstance!=null){
      mInstance.onActivityDestroy();
      HotRefreshManager.getInstance().disConnect();
    }
  }

  @Override
  public boolean handleMessage(Message message) {

    switch (message.what) {
      case Constants.HOT_REFRESH_CONNECT:
        HotRefreshManager.getInstance().connect(message.obj.toString());
        break;
      case Constants.HOT_REFRESH_CONNECT_ERROR:
        Toast.makeText(this, "hot refresh connet error!", Toast.LENGTH_SHORT).show();
        break;
      case Constants.HOT_REFRESH_DISCONNECT:
        HotRefreshManager.getInstance().disConnect();
        break;
      case Constants.HOT_REFRESH_REFRESH:
        loadWXfromService(mUri.toString());
        break;
      default:
        break;
    }
    return true;
  }

  @Override
  public boolean push(String param) {
    Log.i("push", param);
    return false;
  }

  @Override
  public boolean pop(String param) {
    Log.i("pop", param);
    return false;
  }

  @Override
  public boolean setNavBarRightItem(String param) {
    return false;
  }

  @Override
  public boolean clearNavBarRightItem(String param) {
    return false;
  }

  @Override
  public boolean setNavBarLeftItem(String param) {
    return false;
  }

  @Override
  public boolean clearNavBarLeftItem(String param) {
    return false;
  }

  @Override
  public boolean setNavBarMoreItem(String param) {
    return false;
  }

  @Override
  public boolean clearNavBarMoreItem(String param) {
    return false;
  }

  @Override
  public boolean setNavBarTitle(String param) {
    return false;
  }
}
