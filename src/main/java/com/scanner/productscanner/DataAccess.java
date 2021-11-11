package com.scanner.productscanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DataAccess {
    public static void saveOrUpdateProductData(Product product){
        //if p.name LIKE name && p.shopName = shopName then get product and update data
        //else add product to table
        Logger.log(AlgorithmExecutor.class.getName(), "New product -> " + product, Logger.LogLevel.INFO);
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
            Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel.ERROR);
        }
        return document;
    }
}
