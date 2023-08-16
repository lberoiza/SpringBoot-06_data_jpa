package com.springboot.app.exporters.pdf;

import com.lowagie.text.Document;
import com.springboot.app.exporters.ExporterService;

public interface ExporterPDFService<E> extends ExporterService<Document, E> {
}
