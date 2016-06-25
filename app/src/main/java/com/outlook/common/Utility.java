package com.outlook.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.outlook.R;

import java.io.File;

/**
 * Created by sunilkumar on 23/06/16.
 */
public class Utility {

  private static ImageLoader imageLoader;
  private static final int SECOND_IN_A_DAY = 86400;
  private static DisplayImageOptions imageOptions;

  /**
   *
   * @param context application context
   * @return ImageLoader instance, it's caching images for one day(86400 seconds)
   */
  public static ImageLoader getImageLoader(Context context) {
    if (imageLoader == null) {
      File cacheDir = StorageUtils.getCacheDirectory(context);
      ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).diskCache(new LimitedAgeDiskCache(cacheDir, SECOND_IN_A_DAY)).build();
      imageLoader = ImageLoader.getInstance();
      imageLoader.init(config);
    }

    return imageLoader;
  }

  /**
   *
   * @return image options which enables caching on disk as well as memory
   */
  public static DisplayImageOptions getImageOptions() {
    if (imageOptions == null)
      imageOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.placeholder).showImageOnLoading(R.drawable.placeholder).cacheInMemory(true).cacheOnDisk(true).build();
    return imageOptions;
  }

  /**
   *
   * @param context application context
   * @param dp value in dp
   * @return converts dp to px value
   */
  public static int dpToPx(Context context, int dp) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return px;
  }

  /**
   *
   * @param context application context
   * @return true if internet is connected
   */
  public static boolean isNetworkAvailable(Context context) {
    boolean isNetworkAvailable = false;
    ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
    if (activeInfo != null && activeInfo.isConnected()) {
      isNetworkAvailable = true;
    }

    return isNetworkAvailable;
  }

  /**
   *
   * @param activity Activity where dialog is to be shown
   * @param failureMessage Message to be shsown to user
   */
  public static void buildAlertMessageNoInternetConnection(final Activity activity, String failureMessage) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setMessage(failureMessage)
        .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            activity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
          }
        })
        .setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialog) {
            activity.finish();
          }
        });

    final AlertDialog alert = builder.create();
    alert.show();
  }
}
