package com.outlook.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

/**
 * Created by sunilkumar on 25/06/16.
 */
public class OutlookExceptionHandler implements Thread.UncaughtExceptionHandler {
  public final PendingIntent _penIntent;
  Context cont;

  /**
   * Constructor
   *
   * @param intent
   * @param cont
   */
  public OutlookExceptionHandler(PendingIntent intent, Context cont) {
    this._penIntent = intent;
    this.cont = cont;
  }

  public void uncaughtException(Thread t, Throwable e) {
    AlarmManager mgr = (AlarmManager) cont.getSystemService(Context.ALARM_SERVICE);
    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, _penIntent);
    System.exit(2);
  }
}
