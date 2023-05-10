package filesworktests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipTest {
    private ClassLoader cl = ZipTest.class.getClassLoader();
    private String zipName = "testfiles.zip";
    private String csvName = "TestCsv.csv";
    private String xlsxName = "TestExcel.xlsx";
    private String[] expectedCsv = new String[]{"market", "stock"};
    private String expectedXls = "Excel page testing";
    private String pdfExpectedAuthor = "Федор Михайлович Достоевский";
    private String pdfExpectedTitle = "Преступление и наказание";


    @Test
    void csvTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream(zipName);

        ) {
            ZipInputStream zip = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().equals(csvName)) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zip));
                    List<String[]> list = csvReader.readAll();
                    System.out.println(Arrays.toString(list.get(1)));
                    Assertions.assertArrayEquals(expectedCsv, list.get(1));
                }
            }
        }
    }

    @Test
    void xlsxTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream(zipName)) {
            ZipInputStream zip = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().equals(xlsxName)) {
                    XLS xls = new XLS(zip);
                    Assertions.assertEquals(expectedXls, xls.excel.getSheetAt(1)
                            .getRow(4).getCell(3).toString());
                }
            }
        }
    }

    @Test
    void pdfTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream(zipName)) {
            ZipInputStream zip = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().contains("pdf")) {
                    PDF pdf = new PDF(zip);
                    Assertions.assertEquals(pdfExpectedAuthor, pdf.author);
                    Assertions.assertEquals(pdfExpectedTitle, pdf.title);
                }
            }
        }

    }
}
