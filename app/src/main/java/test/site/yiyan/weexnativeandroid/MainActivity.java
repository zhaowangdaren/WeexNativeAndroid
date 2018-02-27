package test.site.yiyan.weexnativeandroid;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import test.site.yiyan.weexnativeandroid.weex.WXActivity;
import test.site.yiyan.weexnativeandroid.weex.https.HotRefreshManager;

public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onWeexStart(View view) {
    String url = ((EditText)findViewById(R.id.et_weex_js_url)).getText().toString();
    String websocketPort = ((EditText)findViewById(R.id.et_weex_websocket_port)).getText().toString();
    if (url.length() == 0) return;
    Intent intent = new Intent(this, WXActivity.class);
    intent.setData(Uri.parse(url));
    startActivity(intent);
  }

  public void onWebSocketConnect(View view) {
    String url = ((EditText)findViewById(R.id.et_weex_websocket_url)).getText().toString();
    HotRefreshManager.getInstance().connect(url);
  }
}
