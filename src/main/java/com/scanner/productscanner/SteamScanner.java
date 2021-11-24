package com.scanner.productscanner;

import org.jsoup.nodes.Document;
import us.codecraft.xsoup.XPathEvaluator;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;

public class SteamScanner extends Scanner {
    private final XPathEvaluator nameXPath =
            Xsoup.compile("//div[@class='apphub_HeaderStandardTop']//div[@class='apphub_AppName']/text()");
    private final XPathEvaluator priceXPath =
            Xsoup.compile("//div[@class='game_area_purchase_game']//div[@class='game_purchase_price price']/text()");
    private final XPathEvaluator discountOriginalPriceXPath =
            Xsoup.compile("//div[@class='game_area_purchase_game']//div[@class='discount_original_price']/text()");
    private final XPathEvaluator discountPriceXPath =
            Xsoup.compile("//div[@class='game_area_purchase_game']//div[@class='discount_final_price']/text()");
    private final XPathEvaluator descriptionXPath =
            Xsoup.compile("//*[@id='game_area_description']/text()");
    private final XPathEvaluator urlXPath =
            Xsoup.compile("//div[@id='search_result_container']//div[@id='search_resultsRows']//a/@href");

    public SteamScanner(){
        super("steam");
    }

    @Override
    public Product processHTMLDocument(Document htmlDocument) {
        Product product = new Product();
        product.shopName = shopName;
        product.name = nameXPath.evaluate(htmlDocument).get();
        product.description = descriptionXPath.evaluate(htmlDocument).get();
        product.mainPrice = priceXPath.evaluate(htmlDocument).get();
        if(product.mainPrice == null || product.mainPrice.isEmpty()){
            product.mainPrice = discountOriginalPriceXPath.evaluate(htmlDocument).get();
            product.discountPrice = discountPriceXPath.evaluate(htmlDocument).get();
        }

        ArrayList<String> languages = new ArrayList<>();
        for(int i=2;;i++){
            String text = Xsoup
                    .compile("//div[@id='languageTable']//table//tr[" + i + "]//td[@class='ellipsis']//text()")
                    .evaluate(htmlDocument)
                    .get();
            if(text != null){
                languages.add(text.trim());
            }
            else {
                break;
            }
        }

        StringBuilder builder = new StringBuilder();
        for(int i=0; i < languages.size(); i++){
            builder.append(languages.get(i));
            if(!((i+1) == languages.size())){
                builder.append(", ");
            }
        }
        product.languages = builder.toString();

        return product.name != null && !product.name.isEmpty() ? product : null;
    }

    @Override
    public void getProductsURLs() {
        Document document = DataAccess.downloadHTMLDocument("https://store.steampowered.com/search/?specials=1");
        productsURLs = (ArrayList<String>) urlXPath.evaluate(document).list();
        for (String url : productsURLs) {
            Logger.log(getClass().getName(), "New url -> " + url, Logger.LogLevel.NONE);
        }
    }
}