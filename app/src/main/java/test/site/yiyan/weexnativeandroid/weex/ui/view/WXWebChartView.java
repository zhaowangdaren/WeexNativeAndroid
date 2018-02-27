package test.site.yiyan.weexnativeandroid.weex.ui.view;

import android.content.Context;

import com.taobao.weex.ui.view.WXWebView;

import test.site.yiyan.weexnativeandroid.weex.ui.component.WebChartTemplate;

/**
 * Created by gtja on 2018/2/23.
 */

public class WXWebChartView extends WXWebView {
  public WXWebChartView(Context context) {
    super(context);
  }
  /**
   * 设置图表的参数，并加载图表
   * @param optionJsonStr 图表配置
   */
  public void setChartOption(String optionJsonStr) {
    super.getWebView().loadDataWithBaseURL("file:////android_asset/www/",
        WebChartTemplate.getBaseHtml(optionJsonStr), "text/html", "UTF-8",
        null);
  }
}
