/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hunor
 */
public class Supplier extends BaseEntity{

    private String name;
    private double rating;

    public Supplier(String id, String name,double rating) {
        super(id);
        this.name = name;
        this.rating=rating;
    }

    public String getName() {
        return name;
    }

    @Override
    public String businessKey() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Supplier[id=" + id + ", name=" + name + "]";
    }
}
