import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class WarehouseManager {

   
    private static String readFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return sb.toString();
    }

    
    private static String getStringValue(String json, String key) {
        try {
            String keySearch = "\"" + key + "\": \"";
            int start = json.indexOf(keySearch) + keySearch.length();
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } catch (Exception e) {
            System.err.println("Error parsing string key: " + key);
            return "";
        }
    }

    
    private static int getIntValue(String json, String key) {
        try {
            String keySearch = "\"" + key + "\": ";
            int start = json.indexOf(keySearch) + keySearch.length();
            int end = json.indexOf(",", start);
            if (end == -1) {
                end = json.indexOf("}", start);
            }
            return Integer.parseInt(json.substring(start, end).trim());
        } catch (Exception e) {
            System.err.println("Error parsing int key: " + key);
            return 0;
        }
    }
    
    private static int getDoubleValue(String json, String key) {
        try {
            String keySearch = "\"" + key + "\": ";
            int start = json.indexOf(keySearch) + keySearch.length();
            int end = json.indexOf(",", start);
            if (end == -1) {
                end = json.indexOf("}", start);
            }
            return Integer.parseInt(json.substring(start, end).trim());
        } catch (Exception e) {
            System.err.println("Error parsing int key: " + key);
            return 0;
        }
    }

    private static String getJsonObject(String fullJson, String key) {
        String keySearch = "\"" + key + "\": {";
        int start = fullJson.indexOf(keySearch) + keySearch.length() - 1;
        int end = fullJson.indexOf("}", start) + 1;
        return fullJson.substring(start, end);
    }

    
    private static ArrayList<String> getJsonArray(String fullJson, String key) {
        ArrayList<String> list = new ArrayList<String>();
        String keySearch = "\"" + key + "\": [";
        int start = fullJson.indexOf(keySearch) + keySearch.length();
        int end = fullJson.indexOf("]", start);

        String arrayContent = fullJson.substring(start, end).trim();
        if (arrayContent.isEmpty()) {
            return list;
        }

        String[] objects = arrayContent.split("\\},\\s*\\{");
        for (int i = 0; i < objects.length; i++) {
            String obj = objects[i];
            if (i == 0 && !obj.startsWith("{")) {
                obj = "{" + obj;
            }
            if (i == objects.length - 1 && !obj.endsWith("}")) {
                obj = obj + "}";
            }
            list.add(obj);
        }
        return list;
    }


    public static void main(String[] args) {
        
        try {

            System.out.println("Reading data.json...");
            String jsonData = readFile("data.json");

            String warehouseJson = getJsonObject(jsonData, "warehouse");
            Warehouse warehouse = new Warehouse(
                    getStringValue(warehouseJson, "id"),
                    getStringValue(warehouseJson, "name")
                    
            );

            ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
            ArrayList<String> supplierJsonList = getJsonArray(jsonData, "suppliers");
            for (int i = 0; i < supplierJsonList.size(); i++) {
                String sJson = supplierJsonList.get(i);
                suppliers.add(new Supplier(
                        getStringValue(sJson, "id"),
                        getStringValue(sJson, "name"),
                        getDoubleValue(sJson, "rating")
                ));
            }

            ArrayList<Product> products = new ArrayList<Product>();
            ArrayList<String> productJsonList = getJsonArray(jsonData, "products");
            for (int i = 0; i < productJsonList.size(); i++) {
                String pJson = productJsonList.get(i);
                products.add(new Product(
                        getStringValue(pJson, "id"),
                        getStringValue(pJson, "sku"),
                        getStringValue(pJson, "name"),
                        getIntValue(pJson, "stock"),
                        getIntValue(pJson, "reorderPoint"),
                        getStringValue(pJson, "supplierId")
                ));
            }

            ArrayList<StockMovement> movements = new ArrayList<StockMovement>();
            ArrayList<String> movementJsonList = getJsonArray(jsonData, "movements");
            for (int i = 0; i < movementJsonList.size(); i++) {
                String mJson = movementJsonList.get(i);
                movements.add(new StockMovement(
                        getStringValue(mJson, "id"),
                        getStringValue(mJson, "sku"),
                        getStringValue(mJson, "type"),
                        getIntValue(mJson, "qty")
                ));
            }
            
            System.out.println("Data parsing complete.");

            warehouse.loadData(products, suppliers, movements);

            warehouse.applyStockMovements();

            warehouse.printSummary();

        } catch (IOException e) {
            System.err.println("FATAL ERROR reading file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("FATAL ERROR processing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}