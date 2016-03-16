/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.model;

/**
 *
 * @author Katie Lin
 */
public class ProductsPurchasedResult implements QueryResult {
    private int customerID;
    private String firstName;
    private String lastName;
    private int quantity;
    
    public ProductsPurchasedResult(int customerID, String firstName, String lastName, int quantity) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.quantity = quantity;
    }
    
    public void setCustomerID(int customerID){
        this.customerID = customerID;
    }
    public int getCustomerID(){
        return this.customerID;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public int getQuantity(){
        return this.quantity;
    }
}
