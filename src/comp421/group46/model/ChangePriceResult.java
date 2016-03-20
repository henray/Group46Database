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
public class ChangePriceResult implements QueryResult {
    private int productID;
    private String productName;
    private double productPrice;
    private int bigInt;
    
    public ChangePriceResult(int productID, String productName, double productPrice, int bigInt) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.bigInt = bigInt;
    }
    
    public int getProductID(){
        return this.productID;
    }
    public String getProductName(){
        return this.productName;
    }
    public double getProductPrice(){
        return this.productPrice;
    }
    public int getBigInt(){
        return this.bigInt;
    }
    public void setProductID(int productID){
        this.productID = productID;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }
    public void setProductPrice(double productPrice){
        this.productPrice = this.productPrice;
    }
    public void setBigInt(int bigInt){
        this.bigInt = bigInt;
    }
}
