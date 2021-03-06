import com.scanner.productscanner.DataAccess;
import com.scanner.productscanner.SteamScanner;
import org.junit.jupiter.api.Test;

public class SteamScannerTest {
    SteamScanner scanner = new SteamScanner();

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
