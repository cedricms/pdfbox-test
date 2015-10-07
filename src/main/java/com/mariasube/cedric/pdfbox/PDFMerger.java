package com.mariasube.cedric.pdfbox;

import java.io.File;

public class PDFMerger {

    /**
     * Constructor
     */
    public PDFMerger() {
        super();
    }

    public static void main(String[] args) {
        PDFMerger pdfMerger = new PDFMerger();
        String sourcePdf1 = "";
        String sourcePdf2 = "";
        String destinationPath = "";
        pdfMerger.mergeDocuments(sourcePdf1, sourcePdf2, destinationPath);
    }

    public File mergeDocuments(String sourcePdf1, String sourcePdf2, String destinationPath) {
        return null;
    }
}
