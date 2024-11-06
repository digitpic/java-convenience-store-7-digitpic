package store.controller;

import store.util.CsvReader;

import java.util.List;

public class StoreController {
    public void run() {
        getInformationFromCsvFile();
    }

    private void getInformationFromCsvFile() {
        List<String> productsInformation = CsvReader.getProductsData();
        List<String> promotionsInformation = CsvReader.getPromotionsData();
    }
}
