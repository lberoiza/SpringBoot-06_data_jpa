package com.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

  private String url;
  private Page<T> page;
  private int totalPages;
  private int elementsPerPage;
  private int currentPage;
  private List<PageItem> pages;

  public PageRender(String url, Page<T> page) {
    this.url = url;
    this.page = page;
    this.totalPages = page.getTotalPages();
    this.elementsPerPage = page.getSize();
    this.currentPage = page.getNumber() + 1;
    this.pages = new ArrayList<PageItem>();

    int from, to;

    if (this.totalPages <= this.elementsPerPage) {
      from = 1;
      to = this.totalPages;
    } else {
      if (this.currentPage <= this.elementsPerPage / 2) {
        from = 1;
        to = this.elementsPerPage;
      } else if (this.currentPage >= this.totalPages - this.elementsPerPage / 2) {
        from = this.totalPages - this.elementsPerPage + 1;
        to = this.elementsPerPage;
      } else {
        from = this.currentPage;
        to = this.elementsPerPage;
      }
    }

    for (int i = 0; i < to; i++) {
      pages.add(new PageItem(from + i, from + i == this.currentPage));
    }

  }

  public String getUrl() {
    return url;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public List<PageItem> getPages() {
    return pages;
  }

  public boolean isFirst() {
    return this.page.isFirst();
  }

  public boolean isLast() {
    return this.page.isLast();
  }

  public boolean hasNext() {
    return this.page.hasNext();
  }
  
  public boolean hasPrevious() {
    return this.page.hasPrevious();
  }

}
