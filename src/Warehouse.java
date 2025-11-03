
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hunor
 */
public class Warehouse extends BaseEntity implements Printable{
    
    private String name;
    private ArrayList<Product> products;
    private ArrayList<Supplier> suppliers;
    private ArrayList<StockMovement> movements;

    
    private static int totalMovementsProcessed = 0;

    public Warehouse(String id, String name) {
        super(id);
        this.name = name;
        
        this.products = new ArrayList<Product>();
        this.suppliers = new ArrayList<Supplier>();
        this.movements = new ArrayList<StockMovement>();
    }

    
    public void loadData(ArrayList<Product> products, ArrayList<Supplier> suppliers, ArrayList<StockMovement> movements) {
        this.products = products;
        this.suppliers = suppliers;
        this.movements = movements;
    }

   
    private Product findProductById(String productId) {
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    
    public void applyStockMovements() {
        System.out.println("Processing stock movements...");
        for (int i = 0; i < movements.size(); i++) {
            StockMovement movement = movements.get(i);
            Product product = findProductById(movement.getProductId());

            if (product != null) {
                
                product.adjustStock(movement);
                
                totalMovementsProcessed++;
            } else {
                System.err.println("Warning: Could not find product for movement ID: " + movement.getProductId());
            }
        }
    }


    @Override
    public String businessKey() {
        return this.name;
    }

    
    @Override
    public void printSummary() {
        System.out.println("Generating report...");
        
        StringBuilder sb = new StringBuilder();
        String hRule = "----------------------------------------";

        sb.append("WAREHOUSE: ").append(this.name).append("\n\n");
        sb.append("Processed stock movements: ").append(totalMovementsProcessed).append("\n\n");

        
        sb.append(String.format("%-8s | %-13s | %-6s | %-7s\n", "SKU", "Name", "Stock", "Status"));
        sb.append(hRule).append("\n");

        
        int reorderCount = 0;
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            String status = p.getStatus();
            if ("REORDER".equals(status)) {
                reorderCount++;
            }
            sb.append(String.format("%-8s | %-13s | %-6d | %-7s\n",
                    p.getSku(),
                    p.getName(),
                    p.getCurrentStock(),
                    status));
        }

        
        sb.append(hRule).append("\n");
        sb.append("Total products: ").append(products.size()).append("\n");
        sb.append("Products below reorder: ").append(reorderCount).append("\n");

        String report = sb.toString();

        
        System.out.println(report);

        
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter("report.txt");
            bw = new BufferedWriter(fw);
            bw.write(report);
            System.out.println("\nSuccessfully wrote to report.txt");
        } catch (IOException e) {
            System.err.println("Error writing report file: " + e.getMessage());
        } finally {
            
            try {
                if (bw != null) bw.close();
                if (fw != null) fw.close();
            } catch (IOException ex) {
                System.err.println("Error closing file writer: " + ex.getMessage());
            }
        }
    }
    
}
