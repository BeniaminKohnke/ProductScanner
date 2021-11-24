import com.scanner.productscanner.DataAccess;
import com.scanner.productscanner.EpicGamesScanner;
import org.junit.jupiter.api.Test;

public class EpicGamesScannerTest {
    EpicGamesScanner scanner = new EpicGamesScanner();

    @Test
    public void getProductsURLsTest(){
        scanner.getProductsURLs();
    }

    @Test
    public void processHTMLDocumentTest(){
        scanner.getProductsURLs();
        for (String url : scanner.productsURLs) {
            scanner.processHTMLDocument(DataAccess.downloadHTMLDocument(url));
        }
    }
}
