import com.scanner.productscanner.DataAccess;
import com.scanner.productscanner.Product;
import org.junit.jupiter.api.Test;

public class DataAccessTest {
    @Test
    public void getUrlFormDBTest(){
        DataAccess.getExistingProducts();
        if(DataAccess.existingURLs != null && !DataAccess.existingURLs.isEmpty()){
            for (String url : DataAccess.existingURLs.get("steam")) {
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

        System.out.println(steamTestProduct);
        DataAccess.saveOrUpdateProductData(steamTestProduct);
    }
}
