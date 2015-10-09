package com.mariasube.cedric.pdfbox;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

import java.io.File;
import java.io.IOException;

public class PDFMerger {

    /**
     * Constructor
     */
    public PDFMerger() {
        super();
    }

    public static void main(String[] args) {
        File baseDir = new File(".");
        System.out.println("Base dir : " + baseDir.getAbsolutePath());
        File[] files = baseDir.listFiles();
        int i = 0;
        while (i < files.length) {
            File file = files[i];
            System.out.println("Child files : " + file.getAbsolutePath());
            i++;
        }

        PDFMerger pdfMerger = new PDFMerger();
        String sourcePdf1 = "/pdf/Pdf1ToMerge.pdf";
        String sourcePdf2 = "/pdf/Pdf1ToMerge.pdf";
        String destinationFilePath = "./pdf/MergedFile.pdf";
        File mergedFile = pdfMerger.mergeDocuments(sourcePdf1, sourcePdf2, destinationFilePath);
        System.out.println("Merged file size :  " + mergedFile.length() + ".");
    }

    public File mergeDocuments(String sourcePdf1, String sourcePdf2, String destinationFilePath) {
        // Clean up destiation file  
        File mergedFile = new File(destinationFilePath);
        if (mergedFile.exists()) {
            if (mergedFile.delete()) {
                System.out.println(mergedFile.getAbsolutePath() + " deleted.");
            } else {
                System.out.println("Unable to delete " + mergedFile.getAbsolutePath() + ".");
            }
        } else {
            System.out.println(mergedFile.getAbsolutePath() + " correctly initialised.");
        }

        try {
            mergedFile.getParentFile().mkdir();
            mergedFile.createNewFile();
        } catch (IOException ioe) {
            System.out.println("Unable to create " + mergedFile.getAbsolutePath());
            ioe.printStackTrace();
        }

        // Merge PDF
        System.out.println("Beginning of merge.");
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();

        pdfMergerUtility.addSource(getClass().getResourceAsStream(sourcePdf1));
        System.out.println("PDF 1 added.");
        pdfMergerUtility.addSource(getClass().getResourceAsStream(sourcePdf2));
        System.out.println("PDF 2 added.");

        pdfMergerUtility.setDestinationFileName(destinationFilePath);
        System.out.println("Merge destination set.");
        try {
            pdfMergerUtility.mergeDocuments();
        } catch (COSVisitorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("End of merge.");
        return mergedFile;
    }
}
