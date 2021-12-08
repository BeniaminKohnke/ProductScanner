package com.scanner.productscanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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
            String sql = "SELECT url FROM [SCANNER].[dbo].[steam];";
            try {
                Statement query = sqlConnection.createStatement();
                ResultSet response = query.executeQuery(sql);
                while(response.next()){
                    existingURLs.add(response.getString("url"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sql = "SELECT url FROM [SCANNER].[dbo].[epicGames];";
            try {
                Statement query = sqlConnection.createStatement();
                ResultSet response = query.executeQuery(sql);
                while(response.next()){
                    existingURLs.add(response.getString("url"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    sqlConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Logger.log(DataAccess.class.getName(), "Algorithm could not connect to database", Logger.LogLevel.NONE);
        }
    }

    public static void saveOrUpdateProductData(Product product){
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SCANNER;", "sa", "JAVA123");
        } catch (SQLException e) {
            Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel.ERROR);
        }
        if (sqlConnection != null) {
            try {
                String sql;
                boolean containsUrl = false;
                for(String url : existingURLs){
                    if(url.equals(product.url)){
                        containsUrl = true;
                        break;
                    }
                }

                if(!containsUrl){
                    sql = "INSERT INTO [SCANNER].[dbo].[" + product.shopName + "] (name, url, imageUrl, lastScan, mainPrice, discountPrice, languages, description) VALUES ("
                            + String.format("'%s', '%s', '%s', %s, '%s', '%s', '%s', '%s'",
                            product.name.replace('\'',' '),
                            product.url,
                            product.imageUrl,
                            "GETDATE()",
                            product.mainPrice.replace('\'',' '),
                            product.discountPrice.replace('\'',' '),
                            product.languages.replace('\'',' '),
                            product.description.replace('\'',' ')) + ");";
                    sqlConnection.createStatement().execute(sql);
                    Logger.log(AlgorithmExecutor.class.getName(), "New product -> " + product.shopName + " -> " + product.name, Logger.LogLevel.NONE);
                } else {
                    sql = "UPDATE [SCANNER].[dbo].[" + product.shopName + "] SET "
                            + String.format("name = '%s', imageUrl = '%s', lastScan = %s, mainPrice = '%s', discountPrice = '%s', languages = '%s', description = '%s'",
                            product.name.replace('\'',' '),
                            product.imageUrl,
                            "GETDATE()",
                            product.mainPrice.replace('\'',' '),
                            product.discountPrice.replace('\'',' '),
                            product.languages.replace('\'',' '),
                            product.description.replace('\'',' '))
                            + "WHERE url LIKE '" + product.url + "';";
                    sqlConnection.createStatement().execute(sql);
                    Logger.log(AlgorithmExecutor.class.getName(), "Updated product -> " + product.shopName + " -> " + product.name, Logger.LogLevel.NONE);
                }
            } catch (SQLException e) {
                Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel.ERROR);
            } finally {
                try {
                    sqlConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Logger.log(DataAccess.class.getName(), "Cannot connect to database", Logger.LogLevel.NONE);
        }
    }

    public static void saveLog(String callerName, String message, Logger.LogLevel level) {
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SCANNER;", "sa", "JAVA123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (sqlConnection != null) {
            String sql = "INSERT INTO [SCANNER].[dbo].[logger] (caller, message, logLevel, date) VALUES ("
                    + String.format("'%s', '%s', '%s', %s", callerName.replace('\'', ' '), message.replace('\'', ' '), level.toString(), "GETDATE()") + ");";
            try {
                sqlConnection.createStatement().execute(sql);
            } catch (SQLException e) {
                Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel.NONE);
            } finally {
                try {
                    sqlConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Logger.log(DataAccess.class.getName(), "Cannot connect to database", Logger.LogLevel.NONE);
        }
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
