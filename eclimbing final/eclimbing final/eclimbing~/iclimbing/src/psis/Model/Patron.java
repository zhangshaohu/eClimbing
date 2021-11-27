/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package psis.Model;

import java.sql.Date;


public class Patron extends GenericUser {

    private String payment;
    private Date start_date;
    private Date end_date;
    private Date suspension_date;
    private String walvor;
    private String access;
    private String semester;
    private int notify,suspension_count;
    
    public Patron(){
        super();
    }
    
    public Patron(String first_name, String last_name, String email, String user_name, String phone,
                        String password,String sex,String payment, Date start_date, Date end_date,
                        String walvor,String access) {
        super(first_name, last_name, email, user_name,  phone, password, sex);
        this.payment = payment;
        this.start_date = start_date;
        this.end_date = end_date;
        this.walvor = walvor;
        this.access = access;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
    
    public String getWalvor() {
        return walvor;
    }

    public void setWalvor(String walvor) {
        this.walvor = walvor;
    }
    
    public Date getStartDate() {
        return start_date;
    }

    public void setStartDate(Date start_date) {
        this.start_date = start_date;
    }
    
    public Date getEndDate() {
        return end_date;
    }

    public void setEndDate(Date end_date) {
        this.end_date = end_date;
    }
    
    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
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
            return semester;
        else if(index == 8)
            return sex;
        else if(index == 9)
            return dob.toString();
        else
            return payment;
    }

    public Date getSuspensionDate() {
        return suspension_date;
    }

    public void setSuspensionDate(Date suspension_date) {
        this.suspension_date = suspension_date;
    }
    
    public int getSuspensionCount() {
        return suspension_count;
    }

    public void setSuspensionCount(int suspension_count) {
        this.suspension_count = suspension_count;
    }
    
    
}
