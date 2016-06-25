package com.outlook;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunilkumar on 23/06/16.
 */
public class PageData {

  @SerializedName("pageid")
  private int pageId;
  @SerializedName("title")
  private String title;
  @SerializedName("thumbnail")
  private ThumbNail thumbNail;

  public int getPageId() {
    return pageId;
  }

  public String getTitle() {
    return title;
  }

  public ThumbNail getThumbNail() {
    return thumbNail;
  }

  @Override
  public String toString() {
    return "PageData{" +
        "thumbNail=" + thumbNail +
        '}';
  }

  public class ThumbNail
  {
    @SerializedName("source")
    private String source;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;

    public String getSource() {
      return source;
    }

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }

    @Override
    public String toString() {
      return "ThumbNail{" +
          "source='" + source + '\'' +
          ", width=" + width +
          ", height=" + height +
          '}';
    }
  }
}
