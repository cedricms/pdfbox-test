package com.mariasube.cedric.pdfbox;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFMergerUtility;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.joda.time.Interval;

public class PDFMerger {

    /**
     * Constructor
     */
    public PDFMerger() {
        super();
    }

    public static void main(String[] args) {
        Calendar startCalendar = Calendar.getInstance();
        PDFMerger pdfMerger = new PDFMerger();
        String sourcePdf1 = "/pdf/Pdf1ToMerge.pdf";
        String sourcePdf2 = "/pdf/Pdf1ToMerge.pdf";
        String destinationFilePath = "./pdf/MergedFile.pdf";
        File mergedFile = pdfMerger.mergeDocuments(sourcePdf1, sourcePdf2, destinationFilePath);
        System.out.println("Merged file size :  " + mergedFile.length() + ".");

        File numberedAndMergedFile = pdfMerger.addPageNumbers(mergedFile, "./pdf/NumberedFile.pdf");
        System.out.println("Numbered file size :  " + numberedAndMergedFile.length() + ".");

        Calendar endCalendar = Calendar.getInstance();
        Interval interval = new Interval(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis());
        System.out.println("Generation time :  " + interval.toString() + ".");
    }

    public File addPageNumbers(File file, String newFilePath) {
        File numberedFile = new File(newFilePath);
        try {
            numberedFile.getParentFile().mkdir();
            numberedFile.createNewFile();
        } catch (IOException ioe) {
            System.out.println("Unable to create " + numberedFile.getAbsolutePath());
            ioe.printStackTrace();
        }
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file);

            List<PDPage> allPages = doc.getDocumentCatalog().getAllPages();
            PDFont font = PDType1Font.HELVETICA_BOLD;
            float fontSize = 36.0f;

            for (int i = 0; i < allPages.size(); i++) {
                PDPage page = (PDPage)allPages.get(i);
                PDRectangle pageSize = page.findMediaBox();
                String message = Integer.toString(i + 1);
                float stringWidth = font.getStringWidth(message) * fontSize / 1000f;
                // calculate to center of the page
                int rotation = page.findRotation();
                boolean rotate = rotation == 90 || rotation == 270;
                float pageWidth = rotate ? pageSize.getHeight() : pageSize.getWidth();
                float pageHeight = rotate ? pageSize.getWidth() : pageSize.getHeight();
                double centeredXPosition = rotate ? pageHeight / 2f : (pageWidth - stringWidth) / 2f;
                double centeredYPosition = rotate ? (pageWidth - stringWidth) / 2f : pageHeight / 2f;
                // append the content to the existing stream
                PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, true, true);
                contentStream.beginText();
                // set font and font size
                contentStream.setFont(font, fontSize);
                // set text color to red
                contentStream.setNonStrokingColor(255, 0, 0);
                if (rotate) {
                    // rotate the text according to the page rotation
                    contentStream.setTextRotation(Math.PI / 2, centeredXPosition, centeredYPosition);
                } else {
                    contentStream.setTextTranslation(centeredXPosition, centeredYPosition);
                }
                contentStream.drawString(message);
                contentStream.endText();
                contentStream.close();
            }

            try {
                doc.save(numberedFile);
            } catch (COSVisitorException cosve) {
                cosve.printStackTrace();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return numberedFile;
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
