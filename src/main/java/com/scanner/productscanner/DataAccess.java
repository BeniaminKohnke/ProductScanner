package com.scanner.productscanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;

public class DataAccess {
    public static ArrayList<String> existingURLs;

    public static void getExistingProducts(){
        existingURLs = new ArrayList<>();
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SCANNER;", "sa", "JAVA123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (sqlConnection != null) {
            String sql = "SELECT url FROM [SCANNER].[dbo].[steam_products];";
            try {
                Statement query = sqlConnection.createStatement();
                ResultSet response = query.executeQuery(sql);
                while(response.next()){
                    existingURLs.add(response.getString("url"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveOrUpdateProductData(Product product){
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SCANNER;", "sa", "JAVA123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (sqlConnection != null) {
            try {
                String sql;
                if(!existingURLs.contains(product.url) && product.shopName.equals("Steam")){
                    sql = "INSERT INTO [SCANNER].[dbo].[steam_products] (name, url, last_scan, main_price, discount_price, languages, description) VALUES ("
                            + String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                            product.name,
                            product.url,
                            product.lastScanDate,
                            product.mainPrice,
                            product.discountPrice,
                            product.languages,
                            product.description) + ");";
                    sqlConnection.createStatement().execute(sql);
                }//else{
                //    sql = "UPDATE [SCANNER].[dbo].[steam_products] SET "
                //            + String.format("name = '%s', last_scan = '%s', main_price = '%s', discount_price = '%s', languages = '%s', description = '%s'",
                //            product.name,
                //            product.lastScanDate,
                //            product.mainPrice,
                //            product.discountPrice,
                //            product.languages,
                //            product.description)
                //            + "WHERE url LIKE '" + product.url + "';";
                //}
                //sqlConnection.createStatement().execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Logger.log(AlgorithmExecutor.class.getName(), "New product -> " + product, Logger.LogLevel.NONE);
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
