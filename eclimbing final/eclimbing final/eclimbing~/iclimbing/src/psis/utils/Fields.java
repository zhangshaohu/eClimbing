/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package psis.utils;

import java.util.Arrays;

/**
 *
 * @author Mashhood
 */
public class Fields {
    public static String[] patronFields = {"Patron ID","First Name","Last Name","User Name","Password","Email","Phone","Semester","Sex","Date Of Birth","Payment"};
    public static String[] patronDBfields = {"patron_id","first_name","last_name","username","password","email","phone","semester","sex","dob"};
    public static String[] patronFieldsWithoutID(){ return Arrays.copyOfRange(patronFields, 1, patronFields.length); }
    public static String[] patronFields(){ return Arrays.copyOfRange(patronFields, 0, patronFields.length); }
    public static String patronID(){ return patronFields[0]; }
    public static String patronFirstName(){ return patronFields[1]; }
    public static String patronLastName(){ return patronFields[2]; }
    public static String patronUserName(){ return patronFields[3]; }
    public static String patronPassword(){ return patronFields[4]; }
    public static String patronEmail(){ return patronFields[5]; }
    public static String patronPhone(){ return patronFields[6]; }
    public static String patronSemester(){ return patronFields[7]; }
    public static String patronSex(){ return patronFields[8]; }
    public static String patronDOB(){ return patronFields[9]; }
    public static String patronPayment(){ return patronFields[10]; }
    
    public static String[] employeeFields = {"Employee ID","First Name","Last Name","User Name","Password","Email","Phone","Sex","Date Of Birth"};
    public static String[] employeeDBfields = {"employee_id","first_name","last_name","username","password","email","phone","sex","dob"};
    public static String[] employeeFieldsWithoutID(){ return Arrays.copyOfRange(employeeFields, 1, employeeFields.length); }
    public static String[] employeeFields(){ return Arrays.copyOfRange(employeeFields, 0, employeeFields.length); }
    public static String employeeID(){ return employeeFields[0]; }
    public static String employeeFirstName(){ return employeeFields[1]; }
    public static String employeeLastName(){ return employeeFields[2]; }
    public static String employeeUserName(){ return employeeFields[3]; }
    public static String employeePassword(){ return employeeFields[4]; }
    public static String employeeEmail(){ return employeeFields[5]; }
    public static String employeePhone(){ return employeeFields[6]; }
    public static String employeeSemester(){ return employeeFields[7]; }
    public static String employeeSex(){ return employeeFields[8]; }
    public static String employeeDOB(){ return employeeFields[9]; }
    
    public static String[] inventoryFields = {"Inventory ID","Name", "Start Date", "End Date"};
    public static String[] inventoryDBFields = {"name","start_date","end_date"};
    public static String[] inventoryFields() { return Arrays.copyOfRange(inventoryFields, 1, inventoryFields.length); };
    public static String inventoryName(){ return inventoryFields[1]; }
    public static String inventoryStartDate(){ return inventoryFields[2]; }
    public static String inventoryEndDate(){ return inventoryFields[3]; }
    public static String inventoryID(){ return inventoryFields[0]; }
    

}
