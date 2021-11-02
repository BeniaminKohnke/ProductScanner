package com.scanner.productscanner;

import org.jsoup.nodes.Document;
import java.util.ArrayList;

public abstract class Scanner {
    public static final int PAGES_TO_SCAN = 1;
    public final String shopName;
    public ArrayList<String> examples = new ArrayList<>();

    public Scanner(String shopName){
        this.shopName = shopName;
    }

    public abstract Product processHTMLDocument(Document htmlDocument);

    public abstract void getExamples();
}
