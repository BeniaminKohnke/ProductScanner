import com.scanner.productscanner.SteamScanner;
import org.junit.jupiter.api.Test;

public class SteamScannerTest {
    SteamScanner scanner = new SteamScanner();

    @Test
    public void getProductsURLsTest(){
        scanner.getProductsURLs();
    }
}
