/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Katie Lin
 */
public class TeamQueryResult {
    private SimpleStringProperty teamName;
    private SimpleIntegerProperty revenue;
    public TeamQueryResult(String teamName, int revenue){
        this.teamName = new SimpleStringProperty(teamName);
        this.revenue = new SimpleIntegerProperty(revenue);
    }
    public String getTeamName(){
        return this.teamName.get();
    }
    public void setTeamName(String teamName){
        this.teamName = new SimpleStringProperty(teamName);
    }
    
    public Integer getRevenue(){
        return this.revenue.get();
    }
    public void setRevenue(Integer revenue){
        this.revenue = new SimpleIntegerProperty(revenue);
    }
    
}
