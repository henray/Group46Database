/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.model.resulthelpers;

import comp421.group46.model.QueryResult;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Katie Lin
 */
public class PlayerQueryResult implements QueryResult {
    private SimpleStringProperty playerName;
    private SimpleIntegerProperty jerseyNumber;
    public PlayerQueryResult(String teamName, int jerseyNumber){
        this.playerName = new SimpleStringProperty(teamName);
        this.jerseyNumber = new SimpleIntegerProperty(jerseyNumber);
    }
    public String getPlayerName(){
        return this.playerName.get();
    }
    public void setPlayerName(String playerName){
        this.playerName = new SimpleStringProperty(playerName);
    }
    
    public Integer getJerseyNumber(){
        return this.jerseyNumber.get();
    }
    public void setJerseyNumber(Integer revenue){
        this.jerseyNumber = new SimpleIntegerProperty(revenue);
    }
    
}
