package watermark.remove;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;

/**
 * Hello world!
 */
public final class App {

    public String inFile, outFile, waterMark;

    private App(String inFile, String outFile, String waterMark) {
        this.inFile = inFile;
        this.outFile = outFile;
        this.waterMark = waterMark;
    }

    private PdfReader readPdf() throws IOException {
        return new PdfReader(this.inFile);
    }

    // add remove teh text
    private void removeText() throws FileNotFoundException, IOException {
       
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(this.inFile), new PdfWriter(this.outFile));
        PdfPage page = pdfDoc.getFirstPage();
        PdfDictionary dict = page.getPdfObject();
 
        PdfObject object = dict.get(PdfName.Contents);
        if (object instanceof PdfStream) {
            PdfStream stream = (PdfStream) object;
            byte[] data = stream.getBytes();
            String replacedData = new String(data).replaceAll("Scanned by CamScanner", "");
            stream.setData(replacedData.getBytes(StandardCharsets.UTF_8));
        }
        pdfDoc.close();
    }
    
    


    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        String waterMark = args.length == 3 ? args[2] : "Scanned by CamScanner";
      
        App app = new App(args[0], args[1], waterMark);
        try{
            app.removeText();
            
        }
        catch(Exception e){
            System.out.println("Please verify the input path");
        }
    }
}
