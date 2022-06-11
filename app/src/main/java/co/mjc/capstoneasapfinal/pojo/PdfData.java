package co.mjc.capstoneasapfinal.pojo;

import java.io.Serializable;


public class PdfData implements Serializable {

    String pdfName;
    String pdfUri;

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public String getPdfUri() {
        return pdfUri;
    }

    public void setPdfUri(String pdfUri) {
        this.pdfUri = pdfUri;
    }
}
