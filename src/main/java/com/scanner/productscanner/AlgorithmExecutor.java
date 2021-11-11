package com.scanner.productscanner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlgorithmExecutor {
    private static final List<Scanner> SCANNERS;
    public static boolean LOCK;

    static  {
        SCANNERS = new ArrayList<>();
        SCANNERS.add(new SteamScanner());
        SCANNERS.add(new EpicGamesScanner());
        LOCK = false;
    }

    public static void scan(){
        if(!LOCK){
            LOCK = true;
            Logger.log(AlgorithmExecutor.class.getName(), "Algorithm started scanning webpages for products' data", Logger.LogLevel.INFO);
            long lastScan = new Date().getTime();
            for (Scanner scanner : SCANNERS) {
                for (String productURL : scanner.productsURLs) {
                    Product product = scanner
                            .processHTMLDocument(DataAccess
                                    .downloadHTMLDocument(productURL));

                    if(product != null){
                        product.url = productURL;
                        product.lastScanDate = lastScan;
                        DataAccess.saveOrUpdateProductData(product);
                    }
                    else {
                        Logger.log(AlgorithmExecutor.class.getName(), "Product's data was not extracted -> " + productURL, Logger.LogLevel.WARN);
                    }
                }
                scanner.productsURLs = new ArrayList<>();
            }
            Logger.log(AlgorithmExecutor.class.getName(), "Algorithm finished scanning webpages for products' data", Logger.LogLevel.INFO);
            LOCK = false;
        }else{
            Logger.log(AlgorithmExecutor.class.getName(), "Other action is being executed", Logger.LogLevel.WARN);
        }
    }

    public static void searchForProducts(){
        if(!LOCK) {
            LOCK = true;
            Logger.log(AlgorithmExecutor.class.getName(), "Algorithm started scanning webpages for products", Logger.LogLevel.INFO);
            for (Scanner scanner : SCANNERS) {
                scanner.getProductsURLs();
            }
            Logger.log(AlgorithmExecutor.class.getName(), "Algorithm finished scanning webpages for products", Logger.LogLevel.INFO);
            LOCK = false;
        }else{
            Logger.log(AlgorithmExecutor.class.getName(), "Other action is being executed", Logger.LogLevel.WARN);
        }
    }
}
