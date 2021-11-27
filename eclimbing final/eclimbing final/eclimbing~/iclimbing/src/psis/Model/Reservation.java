/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psis.Model;

import java.sql.Date;


public class Reservation {
    int id;
    String type, name, email;
    Date from, to;
    String duration, description,status;
    
    public Reservation(){
        
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setFrom(Date from){
        this.from = from;
    }
    
    public void setTo(Date to){
        this.to = to;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public void setDuration(String duration){
        this.duration = duration;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public String getType(){
        return type;
    }
    
    public String getEmail(){
        return email;
    }
    
    public Date getFrom(){
        return from;
    }
    
    public Date getTo(){
        return to;
    }
    
    public String getDuration(){
        return duration;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public String getStatus(){
        return status;
    }
}
