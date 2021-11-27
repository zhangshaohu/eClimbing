/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psis.Model;


public class Report {
    String timestamp,type,patron_id,first_name,last_name,walver;
    
    public Report(){
        
    }
    
    public void setTimeStamp(String timestamp){
        this.timestamp = timestamp;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public void setPatron(String patron_id){
        this.patron_id = patron_id;
    }
    
    public void setFirstName(String first_name){
        this.first_name = first_name;
    }
    
    public void setLastName(String last_name){
        this.last_name = last_name;
    }
    
    public void setWalver(String walver){
        this.walver = walver;
    }
    
    public String getTimeStamp(){
        return this.timestamp;
    }
    
    public String getType(){
        return this.type;
    }
    
    public String getPatron(){
        return this.patron_id;
    }
    
    public String getFirstName(){
        return this.first_name;
    }
    
    public String getLastName(){
        return this.last_name;
    }
    
    public String getWalver(){
        return this.walver;
    }
    
    
}
