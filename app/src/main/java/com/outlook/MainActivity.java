package com.outlook;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.outlook.common.OutlookExceptionHandler;
import com.outlook.common.Utility;
import com.outlook.commonui.DividerViewDecorator;
import com.outlook.commonui.NpaLinearLayoutManager;

public class MainActivity extends AppCompatActivity {

  private EditText searchText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    PendingIntent _pendingInt = PendingIntent.getActivity(getApplicationContext(), 0, intent, intent.getFlags());
    // start handler which starts pending-intent after Application-Crash
    Thread.setDefaultUncaughtExceptionHandler(new OutlookExceptionHandler(_pendingInt, getApplicationContext()));
    setContentView(R.layout.activity_main);
    searchText = (EditText)findViewById(R.id.search_text);
    RecyclerView pageListView = (RecyclerView)findViewById(R.id.page_list_view);
    if(pageListView != null && searchText != null)
    {
      //Defining horizontal layout pattern for recycler view
      pageListView.setLayoutManager(new NpaLinearLayoutManager(this, NpaLinearLayoutManager.HORIZONTAL, false));

      //Adding vertical divider between list items
      pageListView.addItemDecoration(new DividerViewDecorator(Utility.dpToPx(getApplicationContext(), 10)));
      final PageAdapter pageAdapter = new PageAdapter(getApplicationContext(), pageListView);
      pageAdapter.setHasStableIds(true);
      pageListView.setAdapter(pageAdapter);

      searchText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
          pageAdapter.getFilter().filter(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
      });
    }
  }

  @Override
  protected void onResume() {
    if(!Utility.isNetworkAvailable(getApplicationContext()))
    {
      Utility.buildAlertMessageNoInternetConnection(this, getString(R.string.internet_not_connected));
    }
    else
    {
      searchText.requestFocus();
      searchText.post(new Runnable() {
      @Override
      public void run() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchText,InputMethodManager.SHOW_IMPLICIT);
      }
      });
    }
    super.onResume();
  }
}
