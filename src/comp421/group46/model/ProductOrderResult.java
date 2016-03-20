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
public class ProductOrderResult implements QueryResult {
    
    private int orderID;
    
    public ProductOrderResult(int orderID) {
        this.orderID = orderID;
    }
    public void setOrderID(int orderID){
        this.orderID = orderID;
    }
    public int getOrderID() {
        return this.orderID;
    }
}
