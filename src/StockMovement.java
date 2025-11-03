/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hunor
 */
public class StockMovement extends BaseEntity{

    public enum MovementType {
        IN, OUT, UNKNOWN
    }

    private String productId;
    private MovementType type;
    private int quantity;

    public StockMovement(String id, String productId, String typeStr, int quantity) {
        super(id);
        this.productId = productId;
        this.quantity = quantity;

        
        if ("IN".equals(typeStr)) {
            this.type = MovementType.IN;
        } else if ("OUT".equals(typeStr)) {
            this.type = MovementType.OUT;
        } else {
            this.type = MovementType.UNKNOWN;
        }
    }

    public String getProductId() {
        return productId;
    }

    public MovementType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String businessKey() {
        return this.id; // The ID itself is the business key
    }

    @Override
    public String toString() {
        return "StockMovement[id=" + id + ", product=" + productId + ", type=" + type + ", qty=" + quantity + "]";
    }
    
}
