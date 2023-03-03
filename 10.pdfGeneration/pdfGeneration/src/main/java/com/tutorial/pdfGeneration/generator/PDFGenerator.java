package com.tutorial.pdfGeneration.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tutorial.pdfGeneration.entity.Employee;
import com.tutorial.pdfGeneration.repository.EmployeeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class PDFGenerator implements CommandLineRunner {

    @Value("${pdfDir}")
    private String pdfDir;

    @Value("${reportFileName}")
    private String reportFileName;

    @Value("${reportFileNameDateFormat}")
    private String reportFileNameDateFormat;

    @Value("${localDateFormat}")
    private String localDateFormat;

    @Value("${logoImgPath}")
    private String logoImgPath;

    @Value("${logoImgScale}")
    private Float[] logoImgScale;

    @Value("${currencySymbol:}")
    private String currencySymbol;

    @Value("${table_noOfColumns}")
    private int noOfColumns;

    @Value("${table.columnNames}")
    private List<String> columnNames;

    @Autowired
    private EmployeeRepo eRepo;

    private static Font COURIER = new Font(Font.FontFamily.COURIER, 20, Font.BOLD);
    private static Font COURIER_SMALL = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
    private static Font COURIER_SMALL_FOOTER = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

    private void generatePDFReport(){
        Document document = new Document();

        try{
            PdfWriter.getInstance(document,new FileOutputStream(getPdfNameWithDate()));
            document.open();
            addLogo(document);
            addDocTitle(document);
            createTable(document,noOfColumns);
            addFooter(document);
            document.close();
            log.info("PDF is ready");

        }catch (FileNotFoundException | DocumentException e){
            e.printStackTrace();
        }
    }

    private String getPdfNameWithDate() {
        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(reportFileNameDateFormat));
        return pdfDir+reportFileName+"-"+localDateString+".pdf";
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph p2 = new Paragraph();
        leaveEmptyLine(p2, 3);
        p2.setAlignment(Element.ALIGN_MIDDLE);
        p2.add(new Paragraph(
                "------------------------End Of " +reportFileName+"------------------------",
                COURIER_SMALL_FOOTER));

        document.add(p2);
    }

    /*
    * Under paragraph tag we created the Table using PdfTable
    * Iterations for columns to set headers from list application.properties
    * Data is populated in table using repository method
    * Added to document
    */
    private void createTable(Document document, int noOfColumns) throws DocumentException {

        Paragraph paragraph = new Paragraph();
        leaveEmptyLine(paragraph, 3);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(noOfColumns);

        for(int i=0; i<noOfColumns; i++) {
            PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.CYAN);
            table.addCell(cell);
        }

        table.setHeaderRows(1);
        getDbData(table);
        document.add(table);
    }

    /*For all values of entity object new cell is added in oredefined pdftable
    * alignment for cell is done in each iteration*/
    private void getDbData(PdfPTable table) {

        List<Employee> list = eRepo.findAll();
        for (Employee employee : list) {

            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            table.addCell(employee.getEmpId().toString());
            table.addCell(employee.getEmpName());
            table.addCell(employee.getEmpDept());
            table.addCell(currencySymbol + employee.getEmpSal().toString());

            //System.out.println(employee.getEmpName());
        }
    }

    /*
    * Document class
    * Paragraph class object is used to insert the paragraph values via loose coupling
    * and font is also provided using FONT class
    * Alignment can be set for the same using built in methods
    */
    private void addDocTitle(Document document) throws DocumentException {

        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(localDateFormat));
        Paragraph p1 = new Paragraph();
        leaveEmptyLine(p1, 1);
        p1.add(new Paragraph(reportFileName, COURIER));
        p1.setAlignment(Element.ALIGN_CENTER);
        leaveEmptyLine(p1, 1);
        p1.add(new Paragraph("Report generated on " + localDateString, COURIER_SMALL));

        document.add(p1);
    }

    private void leaveEmptyLine(Paragraph paragraph, int number) {

        for (int i = 0; i<number; i++) {
            paragraph.add(new Paragraph(" "));
        }

    }

    /*
    * Adds the image using Document class which allows addition of image
    * Image class is used to get image provided dynamically using class path
    * image can be formatted using Image class build in methods
    */
    private void addLogo(Document document) {

        try {
            Image img = Image.getInstance(logoImgPath);
            img.scalePercent(logoImgScale[0], logoImgScale[1]);
            img.setAlignment(Element.ALIGN_RIGHT);
            document.add(img);
        } catch (DocumentException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void run(String... args) throws Exception {
        generatePDFReport();
    }
}
