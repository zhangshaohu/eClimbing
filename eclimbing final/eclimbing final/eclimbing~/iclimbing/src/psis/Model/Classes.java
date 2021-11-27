/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psis.Model;


public class Classes {
    String id,name,timefrom,timeto,datefrom,dateto,instructor;
    
    public void setId(String id){
        this.id = id;
    }
    
    public void setTimeFrom(String timefrom){
        this.timefrom = timefrom;
    }
    
    public void setTimeTo(String timeto){
        this.timeto = timeto;
    }
    
    public void setDateFrom(String datefrom){
        this.datefrom = datefrom;
    }
    
    public void setDateTo(String dateto){
        this.dateto = dateto;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getId(){
        return id;
    }
    
    public void setInstructor(String instructor){
        this.instructor = instructor;
    }
    
    public String getTimeFrom(){
        return timefrom;
    }
    
    public String getTimeTo(){
        return timeto;
    }
    public String getDateFrom(){
        return datefrom;
    }
    
    public String getDateTo(){
        return dateto;
    }
    
    public String getInstructor(){
        return instructor;
    }
    
    public String getName(){
        return name;
    }
    
}
