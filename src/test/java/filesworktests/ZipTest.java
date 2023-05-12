package filesworktests;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipTest {
    private static final String START_PDF = "Предварительные выводы неутешительны: повышение уровня гражданского";

    private static final String[][] CSV = new String[][] {
            {"Product", "Price", "Owner"},
            {"Note", "23", "Death"},
            {"Apple", "8", "Denis"}
    };

    private static final String XLSX = "value 3";
    ClassLoader classLoader = ZipTest.class.getClassLoader();

    @DisplayName("Проверяем содержимое pdf-файла из zip-архива")
    @Test
    void zipPdfTest() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("Test.zip");
             ZipInputStream zipInputStream = new ZipInputStream(is)) {
            ZipEntry zipEntry;
            while((zipEntry = zipInputStream.getNextEntry()) != null){          // выполняется, пока все файлы из zip-архива не будут прочитаны
                if (zipEntry.getName().contains("pdf")) {                       // если в названии файлы присутствует расширение pdf
                    PDF pdf = new PDF(zipInputStream);
                    Assertions.assertTrue(pdf.text.contains(START_PDF));   //Проверяем, что начало содержимого pdf-файла соответетствует ОР
                }

            }

        }
    }

    @DisplayName("Проверяем содержимое csv-файла из zip-архива")
    @Test
    void zipCsvTest() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("Test.zip");
             ZipInputStream zipInputStream = new ZipInputStream(is)) {
            ZipEntry zipEntry;
            while((zipEntry = zipInputStream.getNextEntry()) != null){
                if (zipEntry.getName().contains("csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zipInputStream));
                    List<String[]> content = csvReader.readAll();
                    Assertions.assertArrayEquals(CSV[0], content.get(0));
                    Assertions.assertArrayEquals(CSV[1], content.get(1));
                    Assertions.assertArrayEquals(CSV[2], content.get(2));

                }

            }

        }
    }



    @DisplayName("Проверяем содержимое xlsx-файла из zip-архива")
    @Test
    void zipXlsTest() throws Exception {

        try (InputStream is = classLoader.getResourceAsStream("Test.zip")) {
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains("xlsx")) {
                    XLS xls = new XLS(zis);
                    Assertions.assertEquals(XLSX,
                            xls.excel.getSheetAt(0)
                                    .getRow(2)
                                    .getCell(1)
                                    .getStringCellValue());
                }
            }
        }
    }

}