import com.scanner.productscanner.AlgorithmExecutor;
import com.scanner.productscanner.DataAccess;
import com.scanner.productscanner.Product;
import org.junit.jupiter.api.Test;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataAccessTest {

    @Test
    public void getUrlFormDBTest(){
        DataAccess.getExistingProducts();
        if(DataAccess.existingURLs != null && !DataAccess.existingURLs.isEmpty()){
            for (String url : DataAccess.existingURLs) {
                System.out.println(url);
            }
        }else{
            System.out.println("NO_URLS_IN_DB");
        }
    }

    @Test
    public void saveSteamProductTest(){
        DataAccess.getExistingProducts();

        Product steamTestProduct = new Product();
        steamTestProduct.name = "TEST_PRODUCT_2";
        steamTestProduct.url = "steam_test.product2.com";
        steamTestProduct.mainPrice = "10 zł";
        steamTestProduct.discountPrice = ("7.45 zł");
        steamTestProduct.shopName = "Steam";

        String lastScan = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String[] split = lastScan.split("/");
        lastScan = split[0] + "-" + AlgorithmExecutor.Months[Integer.parseInt(split[1]) - 1] + "-" + split[2];
        steamTestProduct.lastScanDate = lastScan;

        System.out.println(steamTestProduct);
        DataAccess.saveOrUpdateProductData(steamTestProduct);
    }
}
