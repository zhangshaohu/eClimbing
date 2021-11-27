/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psis.Model;

import java.sql.Date;


public class Inventory {
    int id;
    String name;
    Date start_date, end_date;
    
    public Inventory(){
        
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setName(String name){
        this.name  = name;
    }
    
    public void setStartDate(Date start_date){
        this.start_date = start_date;
    }
    
    public void setEndDate(Date end_date){
        this.end_date = end_date;
    }
    
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public Date getStartDate(){
        return start_date;
    }
    
    public Date getEndDate(){
        return end_date;
    }

}
