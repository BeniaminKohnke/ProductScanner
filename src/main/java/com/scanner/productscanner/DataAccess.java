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
        //if p.name LIKE name && p.shopId = id then get product and update data
        //else add product to table
    }

    public static Document downloadHTMLDocument(String url) {
        Document doc = null;
        try {
            Jsoup.newSession();
            doc = Jsoup.parse(new URL(url), 60000);
            doc.outputSettings()
                    .escapeMode(Entities.EscapeMode.xhtml)
                    .syntax(Document.OutputSettings.Syntax.html)
                    .charset(StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.log(DataAccess.class.getName(), e.getMessage(), Log.LogLevel.ERROR);
        }
        return doc;
    }
}
