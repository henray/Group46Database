/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.model;

import constants.Paths;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Katie Lin
 */
public class TeamsHandler {
    private List<String> teams;
    public TeamsHandler(){
        teams = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(Paths.TEAMS_TEXT));
            String cur = null;
            while((cur = reader.readLine()) != null){
                teams.add(cur);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public List<String> getTeams(){
        return teams;
    }
    public void removeTeam(String team){
        this.teams.remove(team);
    }
}
