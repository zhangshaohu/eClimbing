/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package psis.Model;

public class Employee extends GenericUser {

    private String access;
    
    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
    
    public String get(int index){
        if(index == 0)
            return id;
        else if(index == 1)
            return first_name;
        else if(index == 2)
            return last_name;
        else if(index == 3)
            return user_name;
        else if(index == 4)
            return password;
        else if(index == 5)
            return email;
        else if(index == 6)
            return phone;
        else if(index == 7)
            return sex;
        else
            return dob.toString();
    }
}
