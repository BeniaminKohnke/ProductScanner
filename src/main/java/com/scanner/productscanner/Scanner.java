package com.scanner.productscanner;

import org.jsoup.nodes.Document;
import java.util.ArrayList;

public abstract class Scanner {
    public final String shopName;
    public ArrayList<String> productsURLs = new ArrayList<>();

    public Scanner(String shopName){
        this.shopName = shopName;
    }

    public abstract Product processHTMLDocument(Document htmlDocument);

    public abstract void getProductsURLs();
}
