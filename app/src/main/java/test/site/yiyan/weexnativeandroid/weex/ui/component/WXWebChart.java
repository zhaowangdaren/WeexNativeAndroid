package test.site.yiyan.weexnativeandroid.weex.ui.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.IWebView;

import java.util.HashMap;
import java.util.Map;

import test.site.yiyan.weexnativeandroid.weex.ui.view.WXWebChartView;

/**
 * Created by gtja on 2018/2/23.
 */

public class WXWebChart extends WXComponent {

  @Deprecated
  public WXWebChart(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, String instanceId, boolean isLazy) {
    this(instance,dom,parent,isLazy);
  }

  public WXWebChart(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, boolean isLazy) {
    super(instance, dom, parent, isLazy);
    createWebView();
  }

  protected WXWebChartView mWebView;

  protected void createWebView() {
    mWebView = new WXWebChartView(getContext());
  }

  @Override
  protected View initComponentHostView(@NonNull Context context) {
    mWebView.setOnErrorListener(new IWebView.OnErrorListener() {

      @Override
      public void onError(String type, Object message) {
        fireEvent(type, message);
      }
    });
    mWebView.setOnPageListener(new IWebView.OnPageListener() {
      @Override
      public void onReceivedTitle(String title) {
        if (getDomObject().getEvents().contains(Constants.Event.RECEIVEDTITLE)) {
          Map<String, Object> params = new HashMap<>();
          params.put("title", title);
          fireEvent(Constants.Event.RECEIVEDTITLE, params);
        }
      }

      @Override
      public void onPageStart(String url) {
        if ( getDomObject().getEvents().contains(Constants.Event.PAGESTART)) {
          Map<String, Object> params = new HashMap<>();
          params.put("url", url);
          fireEvent(Constants.Event.PAGESTART, params);
        }
      }

      @Override
      public void onPageFinish(String url, boolean canGoBack, boolean canGoForward) {
        if ( getDomObject().getEvents().contains(Constants.Event.PAGEFINISH)) {
          Map<String, Object> params = new HashMap<>();
          params.put("url", url);
          params.put("canGoBack", canGoBack);
          params.put("canGoForward", canGoForward);
          fireEvent(Constants.Event.PAGEFINISH, params);
        }
      }
    });
    return mWebView.getView();
  }

  @Override
  public void destroy() {
    super.destroy();
    mWebView.destroy();
  }

  @WXComponentProp(name = Constants.Name.CHART_OPTION)
  public void setChartOption(String option) {
    if (TextUtils.isEmpty(option) || getHostView() == null) {
      return;
    }
    if (!TextUtils.isEmpty(option)) {
      mWebView.setChartOption(option);
    }
  }

  private void fireEvent(String type, Object message) {
    if (getDomObject().getEvents().contains(Constants.Event.ERROR)) {
      Map<String, Object> params = new HashMap<>();
      params.put("type", type);
      params.put("errorMsg", message);
      fireEvent(Constants.Event.ERROR, params);
    }
  }
}
