package com.outlook;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.outlook.common.QueryBuilder;
import com.outlook.common.Utility;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sunilkumar on 23/06/16.
 */
public class PageAdapter extends RecyclerView.Adapter<PageAdapter.ViewHolder> implements Filterable {

  private final String TAG = "PageAdapter";
  private final String TAG_QUERY = "query";
  private final String TAG_PAGES = "pages";
  private final Context mContext;
  private final RecyclerView mRecyclerView;
  private ArrayList<PageData> pageDataList = new ArrayList<>();
  private Gson gson = new Gson();

  public PageAdapter(Context context, RecyclerView recyclerView)
  {
    mContext = context;
    mRecyclerView = recyclerView;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_list_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    PageData.ThumbNail thumbNail = getItem(position).getThumbNail();
    if(thumbNail != null)
    {
      ImageSize targetImageSize = new ImageSize(thumbNail.getWidth(), thumbNail.getHeight());
      Utility.getImageLoader(mContext).loadImage(thumbNail.getSource(), targetImageSize, Utility.getImageOptions(), new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String s, View view) {

        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
          holder.thumb.setImageBitmap(bitmap);
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
      });
    }
  }

  @Override
  public long getItemId(int position) {
      return pageDataList.get(position).getPageId();
  }

  public PageData getItem(int position)
  {
      return pageDataList.get(position);
  }

  @Override
  public int getItemCount() {
    if(pageDataList != null)
      return pageDataList.size();
    else
      return 0;
  }

  @Override
  public Filter getFilter() {
    Filter filter = new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence constraint)
      {
        FilterResults filterResults = new FilterResults();

        if (constraint != null)
        {
          ArrayList<PageData> resultPlaceDataList = autocomplete(constraint.toString());
          filterResults.values = resultPlaceDataList;
          filterResults.count = resultPlaceDataList.size();
        }

        return filterResults;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results)
      {
        if (results != null)
        {
          pageDataList = (ArrayList<PageData>) results.values;
          mRecyclerView.getRecycledViewPool().clear();
          notifyDataSetChanged();
        }
      }
    };

    return filter;
  }

  /**
   *
   * @param pageTitle prefix for page
   * @return list of pages corresponding to searched page title
   */
  private synchronized ArrayList<PageData> autocomplete(String pageTitle)
  {
    pageDataList.clear();
    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    try {
      URL url = new URL(QueryBuilder.getSearchPageUrl(URLEncoder.encode(pageTitle, "utf8")));
      conn = (HttpURLConnection) url.openConnection();
      InputStreamReader in = new InputStreamReader(conn.getInputStream());

      // Load the results into a StringBuilder
      int read;
      char[] buff = new char[1024];
      while ((read = in.read(buff)) != -1) {
        jsonResults.append(buff, 0, read);
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return pageDataList;
    } catch (IOException e) {
      e.printStackTrace();
      return pageDataList;
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }

    try {
      Object jObject = new JSONTokener(jsonResults.toString()).nextValue();
      if (jObject instanceof JSONObject) {
        JSONObject jFullObject = new JSONObject(jsonResults.toString());

        if(!jFullObject.isNull(TAG_QUERY) && jFullObject.get(TAG_QUERY) instanceof JSONObject)
        {
          JSONObject jQuery = jFullObject.getJSONObject(TAG_QUERY);

          if(!jQuery.isNull(TAG_PAGES) && jQuery.get(TAG_PAGES) instanceof JSONObject)
          {
            JSONObject jPages = jQuery.getJSONObject(TAG_PAGES);

            Iterator<String> iterator = jPages.keys();

            while(iterator.hasNext())
            {
              String key = iterator.next();
              PageData pageData = gson.fromJson(jPages.getString(key), PageData.class);
              pageDataList.add(pageData);
            }
          }
        }
      }
    }catch(Exception e)
    {
      e.printStackTrace();
    }

    return pageDataList;
  }

  public class ViewHolder extends RecyclerView.ViewHolder
  {
    ImageView thumb;
    public ViewHolder(View itemView) {
      super(itemView);
      thumb = (ImageView) itemView.findViewById(R.id.thumb);
    }
  }
}
