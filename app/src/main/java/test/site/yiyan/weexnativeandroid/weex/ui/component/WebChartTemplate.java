package test.site.yiyan.weexnativeandroid.weex.ui.component;

/**
 * Created by gtja on 2018/2/23.
 */

public class WebChartTemplate {
  private static String baseHtml = "<!DOCTYPE html>\n" +
      "<html>\n" +
      "<head>\n" +
      "  <meta charset=\"utf-8\">" +
      "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">\n" +
      "  <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
      "  <meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
      "  <meta name=\"apple-touch-fullscreen\" content=\"yes\">\n" +
      "  <meta name=\"format-detection\" content=\"telephone=no, email=no\">" +
      "  <title>国泰君安君弘</title>\n" +
      "  <style type=\"text/css\">\n" +
      "    html,body{\n" +
      "      padding: 0;\n" +
      "      margin: 0;\n" +
      "      height: 100%;" +
      "      width: 100%;" +
      "    }\n" +
      "    #chart{\n" +
      "      background-color: #efefef;\n" +
      "    }\n" +
      "  </style>\n" +
      "</head>\n" +
      "<body>\n" +
      "<div id=\"chart\"></div>\n" +
      "</body>\n" +
      "<script type=\"text/javascript\" src=\"echarts.min.js\"></script>\n" +
      "<script type=\"text/javascript\">\n" +
      "  var chartDOM = document.querySelector(\"#chart\")\n" +
      "  chartDOM.style.width = document.body.clientWidth + \"px\"\n" +
      "  chartDOM.style.height = document.body.clientHeight + \"px\"\n" +
      "</script>";

  public static String getBaseHtml(String option) {
    if (option == null || option.equals("")) return baseHtml + "</html>";
    String chartInitJS = "<script type=\"text/javascript\">" +
        "echarts.init(chartDOM).setOption(" + option + ");" +
        "</script></html>";
    return baseHtml + chartInitJS;
  }
}
