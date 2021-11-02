package com.scanner.productscanner;

public class Product {
    public String name = "";
    public String mainPrice = "";
    public String discountPrice = "";
    public String description = "";
    public String languages = "";
    public String url = "";
    public String shopName = "";
    public long lastScanDate = -1;

    @Override
    public String toString() {
        return "Product{" +
                ", name='" + name + '\'' +
                ", mainPrice='" + mainPrice + '\'' +
                ", discountPrice='" + discountPrice + '\'' +
                ", description='" + description + '\'' +
                ", languages='" + languages + '\'' +
                ", url='" + url + '\'' +
                ", shopName=" + shopName +
                ", lastScanDate=" + lastScanDate +
                '}';
    }
}
