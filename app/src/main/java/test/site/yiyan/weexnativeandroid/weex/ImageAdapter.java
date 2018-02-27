package test.site.yiyan.weexnativeandroid.weex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import test.site.yiyan.weexnativeandroid.weex.download.FileDownloadListener;
import test.site.yiyan.weexnativeandroid.weex.download.FileDownloadManager;

/**
 * Created by gtja on 2018/2/10.
 */

public class ImageAdapter implements IWXImgLoaderAdapter, Handler.Callback{
  private ImageView mImageView;
  private Handler mHandler;
  @Override
  public void setImage(final String url, final ImageView view, WXImageQuality quality, final WXImageStrategy strategy) {
    mImageView = view;
    mHandler = new Handler(this);
    downloadSingleFile(url);
  }

  private void downloadSingleFile(final String url) {
    if (url == null) return;
    WXSDKManager.getInstance().postOnUiThread(new Runnable() {
      @Override
      public void run() {
        FileDownloadManager.getInstance(mImageView.getContext())
            .download(url, url.substring(url.lastIndexOf(File.separator)),
                new FileDownloadListener() {
                  @Override
                  public void onStart() {
                    Log.i("start donwloadImage", url);
                  }

                  @Override
                  public void onUpdate(int progress) {
                    Log.i("Image update", "" + progress);

                  }

                  @Override
                  public void onCompleted(String filePath) {
                    Bitmap bitmap = getLocalBitmap(filePath);
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = bitmap;
                    mHandler.sendMessage(message);
                  }

                  @Override
                  public void onFailed(String reason) {
                    Log.e("Downolad Failed", reason);
                  }
                });
      }
    }, 0);
  }

  private static Bitmap getLocalBitmap(String path){
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(path);
      return BitmapFactory.decodeStream(fis);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean handleMessage(Message message) {
    switch (message.what) {
      case 1:
        Bitmap bitmap = (Bitmap) message.obj;
        mImageView.setImageBitmap(bitmap);
    }
    return false;
  }
}
