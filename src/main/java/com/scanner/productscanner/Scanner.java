package com.scanner.productscanner;

import org.jsoup.nodes.Document;
import java.util.ArrayList;

public abstract class Scanner {
    public final String shopName;
    public ArrayList<String> productsURLs = new ArrayList<>();

    public Scanner(String shopName){
        this.shopName = shopName;
    }

    /**
     * Scraper method. Processing "htmlDocument" using xpath to get nodes with expected data.
     * Returns product object if price and name is not null or empty,
     * in other cases returns null.
     */
    public abstract Product processHTMLDocument(Document htmlDocument);

    /**
     * Crawler method. Downloading shop's products' page and
     * then uses xpath to get nodes with urls.
     * Every founded url is added to "productsURLs" list.
     */
    public abstract void getProductsURLs();
}
