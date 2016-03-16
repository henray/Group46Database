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
public class PlayerSaleResult implements QueryResult{
    private int numCustomers;
    
    public PlayerSaleResult(int numCustomers) {
        this.numCustomers = numCustomers;
    }
    
    public void setNumCustomers(int numCustomers){
        this.numCustomers = numCustomers;
    }
    public int getNumCustomers(){
        return this.numCustomers;
    }
}
