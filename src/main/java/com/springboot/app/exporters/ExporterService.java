package com.springboot.app.exporters;

import org.springframework.context.support.MessageSourceAccessor;


public interface ExporterService<D, I> {

  void addDataToDocument(D document, I entity, MessageSourceAccessor messageSourceAccessor);

}
