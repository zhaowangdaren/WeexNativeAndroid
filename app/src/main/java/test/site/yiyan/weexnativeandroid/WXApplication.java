package test.site.yiyan.weexnativeandroid;

import android.app.Application;

import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import test.site.yiyan.weexnativeandroid.weex.ImageAdapter;
import test.site.yiyan.weexnativeandroid.weex.ui.component.WXCustomeComponentType;
import test.site.yiyan.weexnativeandroid.weex.ui.component.WXWebChart;


/**
 * Created by gtja on 2018/2/11.
 */

public class WXApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    InitConfig config=new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
    WXSDKEngine.initialize(this,config);
    try {
      WXSDKEngine.registerComponent(WXCustomeComponentType.WEB_CHART, WXWebChart.class);
    } catch (WXException e) {
      e.printStackTrace();
    }
  }
}
