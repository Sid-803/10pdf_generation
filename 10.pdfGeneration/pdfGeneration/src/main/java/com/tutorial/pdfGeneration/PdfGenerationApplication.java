package com.tutorial.pdfGeneration;

import com.tutorial.pdfGeneration.generator.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class PdfGenerationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfGenerationApplication.class, args);

		/*ApplicationContext ac = SpringApplication.run(PdfGenerationApplication.class, args);
		PDFGenerator pDFGenerator = ac.getBean("pdfGenerator",PDFGenerator.class);
		pDFGenerator.generatePdfReport();*/

	}
}
