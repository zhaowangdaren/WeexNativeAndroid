package test.site.yiyan.weexnativeandroid.weex.https;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import test.site.yiyan.weexnativeandroid.weex.Constants;

/**
 * Created by gtja on 2018/2/10.
 */

public class HotRefreshManager {
  private static final String TAG = "HotRefreshManager";

  private static HotRefreshManager hotRefreshInstance = new HotRefreshManager();
  private WebSocket mWebSocket = null;
  private Handler mHandler = null;

  private HotRefreshManager() {
  }

  public static HotRefreshManager getInstance() {
    return hotRefreshInstance;
  }

  public void setHandler(Handler handler) {
    mHandler = handler;
  }

  public boolean disConnect() {
    if (mWebSocket != null) {
      mWebSocket.close(1000, "activity finish!");
    }
    return true;
  }

  public boolean connect(String url) {
    OkHttpClient httpClient = new OkHttpClient();
    Request request = new Request.Builder().url(url).build();
    WXWebSocketListener listener = new WXWebSocketListener();
    mWebSocket = httpClient.newWebSocket(request, listener);
    httpClient.dispatcher().executorService().shutdown();
    return true;
  }

  class WXWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
      Log.i("WebSocket", "onOpen");
      Log.i("WS", response.toString());
//      webSocket.send("Hello, it's SSaurel !");
//      webSocket.send("What's up ?");
//      webSocket.send(ByteString.decodeHex("deadbeef"));
//      webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
    }
    @Override
    public void onMessage(WebSocket webSocket, String text) {
      Log.i("Receiving : ", text);
      if (text.equals("{\"type\":\"ok\"}")) {
        Log.i("onMessage", "done");
        mHandler.obtainMessage(Constants.HOT_REFRESH_REFRESH).sendToTarget();
      }
    }
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
      Log.i("Receiving bytes : ", bytes.hex());
    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
      webSocket.close(NORMAL_CLOSURE_STATUS, null);
      Log.i("Closing : ", code + " / " + reason);
    }
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
      Log.e("Error", "onFailure");
      if (t!=null) Log.i("Error : ", t.toString());
    }
  }
}
