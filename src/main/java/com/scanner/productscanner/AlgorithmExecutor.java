package com.scanner.productscanner;

import java.util.*;

public class AlgorithmExecutor {
    public enum AlgorithmMode{
        SCAN, SEARCH_FOR_EXAMPLES
    }

    public AlgorithmMode algorithmMode;
    private final List<Scanner> scanners = new ArrayList<>();

    public AlgorithmExecutor() {
        scanners.add(new SteamScanner());
        scanners.add(new EpicGamesScanner());
    }

    public void execute() {
        if(algorithmMode.equals(AlgorithmMode.SCAN)){
            scan();
        }
        if(algorithmMode.equals(AlgorithmMode.SEARCH_FOR_EXAMPLES)){
            searchForExamples();
        }
    }

    private void scan(){
        Log.log(getClass().getName(), "Algorithm started scanning webpages for products' data", Log.LogLevel.INFO);
        long lastScan = new Date().getTime();
        for (Scanner scanner : scanners) {
            for (String example : scanner.examples) {
                Product product = scanner
                        .processHTMLDocument(DataAccess
                                .downloadHTMLDocument(example));

                if(product != null){
                    product.url = example;
                    product.lastScanDate = lastScan;
                    System.out.println(product);
                    DataAccess.saveOrUpdateProductData(product);
                }
                else {
                    Log.log(getClass().getName(), "Product's data was not extracted -> " + example, Log.LogLevel.WARN);
                }
            }
        }
        Log.log(getClass().getName(), "Algorithm finished scanning webpages for products' data", Log.LogLevel.INFO);
    }

    private void searchForExamples(){
        Log.log(getClass().getName(), "Algorithm started scanning webpages for products", Log.LogLevel.INFO);
        for (Scanner scanner : scanners) {
            scanner.getExamples();
        }
        Log.log(getClass().getName(), "Algorithm finished scanning webpages for products", Log.LogLevel.INFO);
    }
}
