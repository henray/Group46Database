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
public class TransferWarehouseResult implements QueryResult{
    private boolean isMoved;
    public TransferWarehouseResult(boolean isMoved){
        this.isMoved = isMoved;
    }
    public boolean getIsMoved(){
        return this.isMoved;
    }
    public void setIsMoved(boolean isMoved){
        this.isMoved = isMoved;
    }
}
