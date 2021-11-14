package com.scanner.productscanner;

import org.jsoup.nodes.Document;
import us.codecraft.xsoup.XPathEvaluator;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;

public class EpicGamesScanner extends Scanner{
    private final XPathEvaluator nameXPath =
            Xsoup.compile("//div[@data-component='TitleSectionLayout']//span[@data-component='Text']/text()");
    private final XPathEvaluator priceXPath =
            Xsoup.compile("//div[@data-component='PriceLayout']//span[@data-component='Text']/text()");
    private final XPathEvaluator discountOriginalPriceXPath =
            Xsoup.compile("//div[@data-component='StorePage']//div[@data-component='DesktopSticky']//div[@data-component='PriceLayout']//span//div[1]//div/text()");
    private final XPathEvaluator discountPriceXPath =
            Xsoup.compile("//div[@data-component='StorePage']//div[@data-component='DesktopSticky']//div[@data-component='PriceLayout']//span//div[2]//span[@data-component='Text']/text()");
    private final XPathEvaluator descriptionXPath =
            Xsoup.compile("//div[@data-component='LineClamp']/div/text()");
    private final XPathEvaluator audioLanguageXPath =
            Xsoup.compile("//div[@data-component='SpecificationsLayout']//li[1]//span[@data-component='Text']/text()");
    private final XPathEvaluator textLanguageXPath =
            Xsoup.compile("//div[@data-component='SpecificationsLayout']//li[2]//span[@data-component='Text']/text()");
    private final ArrayList<XPathEvaluator> urlsXPaths = new ArrayList<>();

    EpicGamesScanner(){
        super("Epic Games Store");
        for(int i=1; i<=40; i++){
            urlsXPaths.add(Xsoup.compile("//section[@data-component='BrowseGrid']//li[" + i + "]//a[@role='link']/@href"));
        }
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
        product.languages = audioLanguageXPath.evaluate(htmlDocument).get() + "\n" + textLanguageXPath.evaluate(htmlDocument).get();
        return product.name != null && !product.name.isEmpty() ? product : null;
    }

    @Override
    public void getProductsURLs() {
        Document previousDocument = null;
        for(int i=0; i < Scanner.PAGES_TO_SCAN; i++){
            Document document = DataAccess.downloadHTMLDocument("https://www.epicgames.com/store/pl/browse?sortBy=title&sortDir=ASC&priceTier=tierDiscouted&count=40&start=" + i);
            if(previousDocument != null){
                String previousValue = urlsXPaths.get(0).evaluate(previousDocument).get();
                String currentValue = urlsXPaths.get(0).evaluate(document).get();
                while(previousValue.equals(currentValue)){
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    document = DataAccess.downloadHTMLDocument("https://www.epicgames.com/store/pl/browse?sortBy=title&sortDir=ASC&priceTier=tierDiscouted&count=40&start=" + i);
                    currentValue = urlsXPaths.get(0).evaluate(document).get();
                }
            }
            previousDocument = document;

            if(document != null){
                for(int j = 1; j <= urlsXPaths.size(); j++){
                    String url = urlsXPaths.get(j - 1).evaluate(document).get();
                    if(url != null) {
                        url = "https://www.epicgames.com" + url;
                        productsURLs.add(url);
                        Logger.log(getClass().getName(), "New url -> " + url, Logger.LogLevel.NONE);
                    }
                }
            }
        }
    }
}
