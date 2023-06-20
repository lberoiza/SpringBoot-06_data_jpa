package com.springboot.app.util.paginator;

public class PageItem {

  private int pageNumber;
  private boolean isCurrentPage;

  public PageItem(int pageNumber, boolean isCurrentPage) {
    this.pageNumber = pageNumber;
    this.isCurrentPage = isCurrentPage;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public boolean isCurrentPage() {
    return isCurrentPage;
  }

}
