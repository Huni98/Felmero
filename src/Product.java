/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hunor
 */
public class Product extends BaseEntity{

    private String sku;
    private String name;
    private int currentStock;
    private int reorderPoint;
    private String supplierId;

    public Product(String id, String sku, String name, int initialStock, int reorderPoint, String supplierId) {
        super(id);
        this.sku = sku;
        this.name = name;
        this.currentStock = initialStock;
        this.reorderPoint = reorderPoint;
        this.supplierId = supplierId;
    }

    
    public void adjustStock(int quantity) {
        this.currentStock += quantity;
        if (this.currentStock < 0) {
            this.currentStock = 0;
        }
    }

    
    public void adjustStock(StockMovement movement) {
        if (!movement.getProductId().equals(this.id)) {
            System.err.println("Warning: wring ID " + movement.getId());
            return;
        }

        int quantity = 0;
        if (movement.getType() == StockMovement.MovementType.IN) {
            quantity = movement.getQuantity();
        } else if (movement.getType() == StockMovement.MovementType.OUT) {
            quantity = -movement.getQuantity();
        }

        
        this.adjustStock(quantity);
    }

    
    public String getStatus() {
        return (this.currentStock <= this.reorderPoint) ? "REORDER" : "OK";
    }

    
    public String getSku() { return sku; }
    public String getName() { return name; }
    public int getCurrentStock() { return currentStock; }

    @Override
    public String businessKey() {
        return this.sku;
    }

    @Override
    public String toString() {
        return "Product[id=" + id + ", sku=" + sku + ", name=" + name + ", stock=" + currentStock + "]";
    }
    
}
