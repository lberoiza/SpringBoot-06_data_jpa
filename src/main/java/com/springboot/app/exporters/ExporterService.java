package com.springboot.app.exporters;

import java.util.Locale;

public interface ExporterService<D, I> {

  void addDataToDocument(D document, I entity, Locale locale);

}
