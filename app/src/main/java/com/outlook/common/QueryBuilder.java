package com.outlook.common;

/**
 * Created by sunilkumar on 23/06/16.
 */
public class QueryBuilder {

  private static final String SEARCH_PAGE_URL = "https://en.wikipedia.org/w/api.php?%20action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=50&pilimit=50&generator=prefixsearch&gpssearch=";

  /**
   *
   * @param pageTitle prefix for page
   * @return url for accessing data
   */
  public static String getSearchPageUrl(String pageTitle)
  {
    StringBuilder sb = new StringBuilder(SEARCH_PAGE_URL);
    sb.append(pageTitle);
    return sb.toString();
  }
}
