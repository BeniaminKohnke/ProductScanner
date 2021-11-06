package com.scanner.productscanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DataAccess {
    public static void saveOrUpdateProductData(Product product){
        //if p.name LIKE name && p.shopName = shopName then get product and update data
        //else add product to table
        Log.log(AlgorithmExecutor.class.getName(), "New product -> " + product, Log.LogLevel.INFO);
    }

    public static Document downloadHTMLDocument(String url) {
        Document document = null;
        try {
            Jsoup.newSession();
            document = Jsoup.parse(new URL(url), 60000);
            document.outputSettings()
                    .escapeMode(Entities.EscapeMode.xhtml)
                    .syntax(Document.OutputSettings.Syntax.html)
                    .charset(StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.log(DataAccess.class.getName(), e.getMessage(), Log.LogLevel.ERROR);
        }
        return document;
    }
}
