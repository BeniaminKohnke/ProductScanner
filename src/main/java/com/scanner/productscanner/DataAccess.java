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
    public static HashMap<String, ArrayList<String>> existingURLs;

    static {
        existingURLs = new HashMap<>();
        existingURLs.put("steam", new ArrayList<>());
        existingURLs.put("epicGames", new ArrayList<>());

        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SCANNER;", "sa", "JAVA123");
        } catch (SQLException e) {
            Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel._ERROR);
        } finally {
            if(sqlConnection != null){
                Logger.log(DataAccess.class.getName(), "Initial connection was performed", Logger.LogLevel._INFO);
                try {
                    sqlConnection.close();
                } catch (SQLException e) {
                    Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel._ERROR);
                }
            }
        }
    }

    public static void getExistingProducts(){
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SCANNER;", "sa", "JAVA123");
        } catch (SQLException e) {
            Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel._ERROR);
        }
        if (sqlConnection != null) {
            String sql = "SELECT url FROM [SCANNER].[dbo].[steam];";
            try {
                Statement query = sqlConnection.createStatement();
                ResultSet response = query.executeQuery(sql);
                while(response.next()){
                    existingURLs.get("steam").add(response.getString("url"));
                }
            } catch (SQLException e) {
                Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel._ERROR);
            }

            sql = "SELECT url FROM [SCANNER].[dbo].[epicGames];";
            try {
                Statement query = sqlConnection.createStatement();
                ResultSet response = query.executeQuery(sql);
                while(response.next()){
                    existingURLs.get("epicGames").add(response.getString("url"));
                }
            } catch (SQLException e) {
                Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel._ERROR);
            } finally {
                try {
                    sqlConnection.close();
                } catch (SQLException e) {
                    Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel._ERROR);
                }
            }
        } else {
            Logger.log(DataAccess.class.getName(), "Algorithm could not connect to database", Logger.LogLevel._WARN);
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
                for(String url : existingURLs.get(product.shopName)){
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
                    Logger.log(AlgorithmExecutor.class.getName(), "New product -> " + product.shopName + " -> " + product.name, Logger.LogLevel._INFO);
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
                    Logger.log(AlgorithmExecutor.class.getName(), "Updated product -> " + product.shopName + " -> " + product.name, Logger.LogLevel._INFO);
                }
            } catch (SQLException e) {
                Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel.ERROR);
            } finally {
                try {
                    sqlConnection.close();
                } catch (SQLException e) {
                    Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel._ERROR);
                }
            }
        } else {
            Logger.log(DataAccess.class.getName(), "Algorithm could not connect to database", Logger.LogLevel._WARN);
        }
    }

    public static void saveLog(String callerName, String message, Logger.LogLevel level) {
        Connection sqlConnection = null;
        try {
            sqlConnection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SCANNER;", "sa", "JAVA123");
        } catch (SQLException e) {
            Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel._ERROR);
        }
        if (sqlConnection != null) {
            String sql = "INSERT INTO [SCANNER].[dbo].[logger] (caller, message, logLevel, date) VALUES ("
                    + String.format("'%s', '%s', '%s', %s", callerName.replace('\'', ' '), message.replace('\'', ' '), level.toString(), "GETDATE()") + ");";
            try {
                sqlConnection.createStatement().execute(sql);
            } catch (SQLException e) {
                Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel._ERROR);
            } finally {
                try {
                    sqlConnection.close();
                } catch (SQLException e) {
                    Logger.log(DataAccess.class.getName(), e.getMessage(), Logger.LogLevel._ERROR);
                }
            }
        } else {
            Logger.log(DataAccess.class.getName(), "Algorithm could not connect to database", Logger.LogLevel._WARN);
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
