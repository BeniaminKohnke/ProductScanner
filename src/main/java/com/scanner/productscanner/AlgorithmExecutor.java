package com.scanner.productscanner;

import java.util.ArrayList;

public class AlgorithmExecutor {
    private static final ArrayList<Scanner> SCANNERS = new ArrayList<>();
    public static boolean LOCK;

    static {
        SCANNERS.add(new SteamScanner());
        SCANNERS.add(new EpicGamesScanner());
        LOCK = false;
    }

    public static void scan(){
        if(!LOCK){
            LOCK = true;
            DataAccess.getExistingProducts();
            Logger.log(AlgorithmExecutor.class.getName(), "Algorithm started scanning webpages for products' data", Logger.LogLevel.INFO);
            for (Scanner scanner : SCANNERS) {

                //adding existing products to queue
                for (String productURL : DataAccess.existingURLs.get(scanner.shopName)){
                    boolean isInCollection = false;
                    for (String url : scanner.productsURLs) {
                        if(productURL.equals(url)){
                            isInCollection = true;
                            break;
                        }
                    }
                    if(!isInCollection){
                        scanner.productsURLs.add(productURL);
                    }
                }

                for (String productURL : scanner.productsURLs) {
                    Product product = scanner
                            .processHTMLDocument(DataAccess
                                    .downloadHTMLDocument(productURL));

                    if(product != null){
                        product.url = productURL;
                        DataAccess.saveOrUpdateProductData(product);
                    } else {
                        Logger.log(AlgorithmExecutor.class.getName(), "Product's data was not extracted -> " + scanner.shopName + " -> " + productURL, Logger.LogLevel.WARN);
                    }
                }
                scanner.productsURLs = new ArrayList<>();
            }
            Logger.log(AlgorithmExecutor.class.getName(), "Algorithm finished scanning webpages for products' data", Logger.LogLevel.INFO);
            LOCK = false;
        } else {
            Logger.log(AlgorithmExecutor.class.getName(), "Other action is being executed", Logger.LogLevel._WARN);
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
        } else {
            Logger.log(AlgorithmExecutor.class.getName(), "Other action is being executed", Logger.LogLevel.WARN);
        }
    }
}
