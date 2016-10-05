package es.unizar.es.foodnet.model.entity;

import org.springframework.data.annotation.Id;

/**
 * Created by Fran Menendez Moya on 5/10/16.
 */
public class Product {

    @Id
    private String id;

    private String name;
    private String brand;

    //Mandatory in spite of being useless
    public Product() {}

    public Product(String name, String lastName) {

        this.name = name;
        this.brand = lastName;
    }

    @Override

    public String toString() {
        return String.format(
                "Product[id=%s, name='%s', brand='%s']",
                id, name, brand);
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
