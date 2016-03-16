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
public class PlaceOrderResult {
    private boolean isPlaced;
    public PlaceOrderResult(boolean isPlaced) {
        this.isPlaced = isPlaced;
    }
    public void setIsPlaced(boolean isPlaced){
        this.isPlaced = isPlaced;
    }
    public boolean getIsPlaced() {
        return this.isPlaced;
    }
}
