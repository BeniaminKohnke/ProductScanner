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
    private final XPathEvaluator imageXPath =
            Xsoup.compile("//div[@data-component='PDPSidebarLogo']//div[@data-component='Picture']//img/@src");
    private final ArrayList<XPathEvaluator> urlsXPaths = new ArrayList<>();

    public EpicGamesScanner(){
        super("epicGames");
        for(int i=1; i<=50; i++){
            urlsXPaths.add(Xsoup.compile("//section[@data-component='BrowseGrid']//li[" + i + "]//a[@role='link']/@href"));
        }
    }

    @Override
    public Product processHTMLDocument(Document htmlDocument) {
        Product product = new Product();
        product.shopName = shopName;
        product.name = nameXPath.evaluate(htmlDocument).get();
        product.description = descriptionXPath.evaluate(htmlDocument).get();
        if(product.description == null) {
            product.description = "";
        }

        product.mainPrice = priceXPath.evaluate(htmlDocument).get();
        if(product.mainPrice == null || product.mainPrice.isEmpty()){
            product.mainPrice = discountOriginalPriceXPath.evaluate(htmlDocument).get();
            product.discountPrice = discountPriceXPath.evaluate(htmlDocument).get();
            if(product.mainPrice == null){
                return null;
            }
        }
        if(product.discountPrice == null){
            product.discountPrice = "";
        }

        product.imageUrl = imageXPath.evaluate(htmlDocument).get();
        if(product.imageUrl == null){
            product.imageUrl = "";
        }

        String audio = audioLanguageXPath.evaluate(htmlDocument).get();
        String text = textLanguageXPath.evaluate(htmlDocument).get();
        if(audio != null || text != null) {
            if(audio != null){
                product.languages = audio;
            }
            if(text != null){
                product.languages += " " + text;
                product.languages = product.languages.trim();
            }
        } else {
            product.languages = "";
        }
        return product.name != null && !product.name.isEmpty() ? product : null;
    }

    @Override
    public void getProductsURLs() {
        Document document = DataAccess.downloadHTMLDocument("https://www.epicgames.com/store/pl/browse?sortBy=releaseDate&sortDir=DESC&priceTier=tierDiscouted&count=50&start=0");
        if(document != null){
            for(int i = 1; i <= urlsXPaths.size(); i++){
                String url = urlsXPaths.get(i - 1).evaluate(document).get();
                if(url != null) {
                    url = "https://www.epicgames.com" + url;
                    productsURLs.add(url);
                    Logger.log(getClass().getName(), "New url -> " + shopName + " -> " + url, Logger.LogLevel._INFO);
                }
            }
        } else {
            Logger.log(getClass().getName(), "Algorithm could not download document", Logger.LogLevel.WARN);
        }
    }
}
