/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package psis.Controller;

import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import psis.Model.Classes;
import psis.Model.Employee;
import psis.Model.Inventory;
import psis.Model.Patron;
import psis.Model.Report;
import psis.Model.Reservation;


public class UserController extends DBcontrol {
    
    public UserController(DBcontrol database){
        this.dbCon = database.dbCon;
        try {
            this.stmt = this.dbCon.createStatement();
        } catch (SQLException ex) {
            
        }
    }
    // returns ROLE after successful login otherwise "error"
    public String login(Patron user){
//        String role = "";
//        try {
//            this.rs = this.stmt.executeQuery("SELECT * FROM USER WHERE user_name='"+user.getName()+"' and password='"+user.getPassword()+"';");
//            if(rs.next()){
//                role = rs.getString("role");
//            } else {
//              role = "error";
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return role;
        return "";
    }
            // returns ROLE after successful login otherwise "error"
    public Patron patronCheckin(Patron user){
        String id = String.valueOf(user.getId());
        System.out.println("SELECT * FROM patron WHERE patron_id='"+id.substring(0,6)+"' ");
        try {
            this.rs = this.stmt.executeQuery("SELECT * FROM patron WHERE patron_id='"+id.substring(0,6)+"' ");
            if(rs.next()){
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setDOB(rs.getDate("dob"));
                user.setPayment(rs.getString("payment"));
                user.setStartDate(rs.getDate("start_date"));
                user.setEndDate(rs.getDate("end_date"));
                user.setAccess(rs.getString("access"));
                user.setSex(rs.getString("sex"));
                user.setWalvor(rs.getString("walvor"));
                
            } else {
              user.setAccess("error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
        // returns ROLE after successful login otherwise "error"
    public Patron patronlogin(Patron user){
        try {
            this.rs = this.stmt.executeQuery("SELECT * FROM patron WHERE patron_id='"+user.getUserName()+"' and password='"+user.getPassword()+"';");
            if(rs.next()){
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setDOB(rs.getDate("dob"));
                user.setPayment(rs.getString("payment"));
                user.setStartDate(rs.getDate("start_date"));
                user.setEndDate(rs.getDate("end_date"));
                user.setAccess(rs.getString("access"));
                user.setSex(rs.getString("sex"));
                user.setWalvor(rs.getString("walvor"));
            } else {
              user.setAccess("error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
            // returns ROLE after successful login otherwise "error"
    public String employeelogin(Patron user){
        String role = "";
        try {
            this.rs = this.stmt.executeQuery("SELECT * FROM employee WHERE username='"+user.getUserName()+"' and password='"+user.getPassword()+"';");
            if(rs.next()){
                role = rs.getString("access");
            } else {
              role = "error";
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return role;
    }
            // returns ROLE after successful login otherwise "error"
    public String adminlogin(Patron user){
        String role = "";
        try {
            this.rs = this.stmt.executeQuery("SELECT * FROM admin WHERE username='"+user.getUserName()+"' and password='"+user.getPassword()+"';");
            if(rs.next()){
                role = rs.getString("access");
            } else {
              role = "error";
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return role;
    }
    
    public int addPatron(Component c,Patron user){
        int status = 0;
        String id = "";
        try {
            this.rs = this.stmt.executeQuery("SELECT * FROM patron WHERE email='"+user.getEmail()+"';");
            if(rs.next()){
                JOptionPane.showMessageDialog(c, "Email already taken." , "Can't Create User", JOptionPane.ERROR_MESSAGE);
            } else {
                String query = null;
                if(user.getPayment() != null)
                    query = "INSERT INTO patron (patron_id,first_name,last_name,dob,sex,username,password,email,phone,semester,payment) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                else
                    query = "INSERT INTO patron (patron_id,first_name,last_name,dob,sex,username,password,email,phone,semester) VALUES (?,?,?,?,?,?,?,?,?,?)";
                Random rn = new Random();
                this.pStmt = this.dbCon.prepareStatement(query);
                this.pStmt.setString(1, rn.nextInt(9)+""+rn.nextInt(9)+""+rn.nextInt(9)+""+rn.nextInt(9)+""+rn.nextInt(9)+""+rn.nextInt(9));
                this.pStmt.setString(2, user.getFirstName());
                this.pStmt.setString(3, user.getLastName());
                this.pStmt.setDate(4, user.getDOB());
                this.pStmt.setString(5, user.getSex());
                this.pStmt.setString(6, user.getUserName());
                this.pStmt.setString(7, user.getPassword());
                this.pStmt.setString(8, user.getEmail());
                this.pStmt.setString(9, user.getPhone());
                this.pStmt.setString(10, user.getSemester());
                if(user.getPayment() != null)
                    this.pStmt.setString(11, user.getPayment());
                status = this.pStmt.executeUpdate();
            if(status > 0)
                id = this.getPatronID(user);
                final String patronid = id;
                JOptionPane.showMessageDialog(c, "New record successfully added.\nYour Patron ID is : "+id, "Success !", JOptionPane.INFORMATION_MESSAGE);
                SwingWorker worker = new SwingWorker<Void, Void>() {
                    @Override
                    public Void doInBackground() {
                        SendEmail.send(user.getEmail(), "You are registerted with i-Climbing.", "Your patron ID is"+patronid+"." );
                        return null;
                    }
                    @Override
                    public void done() {
                    }
                };
                worker.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBcontrol.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(c, "Something went wrong.\n\nCheck whether your input is correct or not?\nIf yes, this must be some system errror." , "Exception Occured", JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
    
    public int addEmployee(Component c,Employee user){
        int status = 0;
        String id = "";
        try {
            this.rs = this.stmt.executeQuery("SELECT * FROM employee WHERE email='"+user.getEmail()+"';");
            if(rs.next()){
                JOptionPane.showMessageDialog(c, "Email already taken." , "Can't Create User", JOptionPane.ERROR_MESSAGE);
            } else {
                String query = "INSERT INTO employee (first_name,last_name,dob,sex,username,password,email,phone) VALUES (?,?,?,?,?,?,?,?)";
                this.pStmt = this.dbCon.prepareStatement(query);
                this.pStmt.setString(1, user.getFirstName());
                this.pStmt.setString(2, user.getLastName());
                this.pStmt.setDate(3, user.getDOB());
                this.pStmt.setString(4, user.getSex());
                this.pStmt.setString(5, user.getUserName());
                this.pStmt.setString(6, user.getPassword());
                this.pStmt.setString(7, user.getEmail());
                this.pStmt.setString(8, user.getPhone());
                
                status = this.pStmt.executeUpdate();
            if(status > 0)
                JOptionPane.showMessageDialog(c, "New record successfully added.", "Success !", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBcontrol.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(c, "Something went wrong.\n\nCheck whether your input is correct or not?\nIf yes, this must be some system errror." , "Exception Occured", JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
    
    public int SaveCheckin(Patron user,String type){
        int affected = 0;
        try {
            String query = "INSERT INTO checkin (user_type,patron_id) VALUES (?,?)";
            this.pStmt = this.dbCon.prepareStatement(query);
            this.pStmt.setString(1, type);
            this.pStmt.setDouble(2, Double.parseDouble(user.getId()));
            affected = this.pStmt.executeUpdate();
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public int SaveDemographicCheckin(Patron user){
        int affected = 0;
        try {
            String query = "INSERT INTO checkin (user_type,first_name,last_name,walvor) VALUES (?,?,?,?)";
            this.pStmt = this.dbCon.prepareStatement(query);
            this.pStmt.setString(1, "Demographic");
            this.pStmt.setString(2, user.getFirstName());
            this.pStmt.setString(3, user.getLastName());
            this.pStmt.setString(4, user.getWalvor());
            
            affected = this.pStmt.executeUpdate();
        } catch (SQLException ex) {
            
        }
        return affected;
    }
            // returns ROLE after successful login otherwise "error"
    public String getPatronID(Patron user){
        String role = "";
        try {
            this.rs = this.stmt.executeQuery("SELECT * FROM patron WHERE username='"+user.getUserName()+"' ");
            if(rs.next()){
                role = rs.getString("patron_id");
            } else {
              role = "error";
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return role;
    }
    
    public Patron getPatron(Patron user){
        try {
            this.rs = this.stmt.executeQuery("SELECT * FROM patron WHERE patron_id='"+user.getUserName()+"' ");
            if(rs.next()){
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setDOB(rs.getDate("dob"));
                user.setPayment(rs.getString("payment"));
                user.setPassword(rs.getString("password"));
                user.setStartDate(rs.getDate("start_date"));
                user.setEndDate(rs.getDate("end_date"));
                user.setAccess(rs.getString("access"));
                user.setSex(rs.getString("sex"));
                user.setWalvor(rs.getString("walvor"));
                user.setSemester(rs.getString("semester"));
                user.setUserName(rs.getString("username"));
                user.setNotify(rs.getInt("notification"));
                user.setId(rs.getString("patron_id"));
                user.setSuspensionCount(rs.getInt("suspension_count"));
                user.setSuspensionDate(rs.getDate("suspension_date"));
            } else {
              user.setAccess("error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
    
    public int editPatron(Patron user){
        String query = "UPDATE patron SET username='"+user.getUserName()+"',first_name='"+user.getFirstName()+"',last_name='"+user.getLastName()+"',phone='"+user.getPhone()+"',password='"+user.getPassword()+"',dob='"+user.getDOB()+"',payment='"+user.getPayment()+"' where email='"+user.getEmail()+"';";
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public Employee getEmployee(Employee user){
        try {
            this.rs = this.stmt.executeQuery("SELECT * FROM employee WHERE employee_id='"+user.getId()+"' ");
            if(rs.next()){
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setDOB(rs.getDate("dob"));
                user.setPassword(rs.getString("password"));
                user.setSex(rs.getString("sex"));
                user.setUserName(rs.getString("username"));
                user.setAccess("yes");
            } else {
              user.setAccess("error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
    
    public int editEmployee(Employee user){
        String query = "UPDATE employee SET username='"+user.getUserName()+"',first_name='"+user.getFirstName()+"',last_name='"+user.getLastName()+"',phone='"+user.getPhone()+"',password='"+user.getPassword()+"',dob='"+user.getDOB()+"' where email='"+user.getEmail()+"';";
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public int editPatronNotify(Patron user){
        String query = "UPDATE patron SET notification='"+user.getNotify()+"' where email='"+user.getEmail()+"';";
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public int editPatronWalver(Patron user){
        String query = "UPDATE patron SET walvor='"+user.getWalvor()+"' where email='"+user.getEmail()+"';";
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public ArrayList<Employee> getAllEmployee(){
        ArrayList<Employee> employees = new ArrayList<Employee>();
        try {
            Employee employee = new Employee();
            this.rs = this.stmt.executeQuery("SELECT * FROM employee");
            if(rs.next()){
                employee = new Employee();
                    employee.setFirstName(rs.getString("first_name"));
                    employee.setLastName(rs.getString("last_name"));
                    employee.setEmail(rs.getString("email"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setDOB(rs.getDate("dob"));
                    employee.setSex(rs.getString("sex"));
                    employee.setUserName(rs.getString("username"));
                    employee.setId(rs.getString("employee_id"));
                    employees.add(employee);
                while(rs.next()){
                    employee = new Employee();
                    employee.setFirstName(rs.getString("first_name"));
                    employee.setLastName(rs.getString("last_name"));
                    employee.setEmail(rs.getString("email"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setDOB(rs.getDate("dob"));
                    employee.setSex(rs.getString("sex"));
                    employee.setUserName(rs.getString("username"));
                    employee.setId(rs.getString("employee_id"));
                    employees.add(employee);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return employees;
    }
    
    public ArrayList<Patron> getAllPatrons(){
        ArrayList<Patron> patrons = new ArrayList<Patron>();
        try {
            Patron patron = new Patron();
            this.rs = this.stmt.executeQuery("SELECT * FROM patron");
            if(rs.next()){
                patron = new Patron();
                    patron.setFirstName(rs.getString("first_name"));
                    patron.setLastName(rs.getString("last_name"));
                    patron.setEmail(rs.getString("email"));
                    patron.setPhone(rs.getString("phone"));
                    patron.setDOB(rs.getDate("dob"));
                    patron.setSex(rs.getString("sex"));
                    patron.setUserName(rs.getString("username"));
                    patron.setId(rs.getString("patron_id"));
                    patrons.add(patron);
                while(rs.next()){
                    patron = new Patron();
                    patron.setFirstName(rs.getString("first_name"));
                    patron.setLastName(rs.getString("last_name"));
                    patron.setEmail(rs.getString("email"));
                    patron.setPhone(rs.getString("phone"));
                    patron.setDOB(rs.getDate("dob"));
                    patron.setSex(rs.getString("sex"));
                    patron.setUserName(rs.getString("username"));
                    patron.setId(rs.getString("patron_id"));
                    patrons.add(patron);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return patrons;
    }
    
    public ArrayList<Inventory> getAllInventories(){
        ArrayList<Inventory> inventories = new ArrayList<Inventory>();
        try {
            Inventory inventory = new Inventory();
            this.rs = this.stmt.executeQuery("SELECT * FROM inventory");
            if(rs.next()){
                inventory = new Inventory();
                    inventory.setId(rs.getInt("inventory_id"));
                    inventory.setName(rs.getString("name"));
                    inventory.setStartDate(rs.getDate("start_date"));
                    inventory.setEndDate(rs.getDate("end_date"));
                    inventories.add(inventory);
                while(rs.next()){
                    inventory = new Inventory();
                    inventory.setId(rs.getInt("inventory_id"));
                    inventory.setName(rs.getString("name"));
                    inventory.setStartDate(rs.getDate("start_date"));
                    inventory.setEndDate(rs.getDate("end_date"));
                    inventories.add(inventory);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inventories;
    }
    
    public ArrayList<Reservation> getAllReservations(){
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        try {
            Reservation reservation = new Reservation();
            this.rs = this.stmt.executeQuery("SELECT * FROM reservation");
            if(rs.next()){
                reservation = new Reservation();
                    reservation.setId(rs.getInt("reservation_id"));
                    reservation.setName(rs.getString("name"));
                    reservation.setFrom(rs.getDate("from_date"));
                    reservation.setEmail(rs.getString("email"));
                    reservation.setTo(rs.getDate("to_date"));
                    reservation.setDuration(rs.getString("duration"));
                    reservation.setType(rs.getString("type"));
                    reservation.setDescription(rs.getString("description"));
                    reservation.setStatus(rs.getString("status"));
                    reservations.add(reservation);
                while(rs.next()){
                    reservation = new Reservation();
                    reservation.setId(rs.getInt("reservation_id"));
                    reservation.setName(rs.getString("name"));
                    reservation.setEmail(rs.getString("email"));
                    reservation.setFrom(rs.getDate("from_date"));
                    reservation.setTo(rs.getDate("to_date"));
                    reservation.setDuration(rs.getString("duration"));
                    reservation.setType(rs.getString("type"));
                    reservation.setDescription(rs.getString("description"));
                    reservation.setStatus(rs.getString("status"));
                    reservations.add(reservation);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reservations;
    }
    
    public int deletePatron(String id){
        String query = "DELETE from patron where patron_id='"+id+"';";
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public int deleteEmployee(String id){
        String query = "DELETE from employee where employee_id='"+id+"';";
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public int deleteInventory(String id){
        String query = "DELETE from inventory where inventory_id='"+id+"';";
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public int addInventory(Component c,Inventory inv){
        int status = 0;
        String id = "";
        try {
            String query = "INSERT INTO inventory (name,start_date,end_date) VALUES (?,?,?)";
            this.pStmt = this.dbCon.prepareStatement(query);
            this.pStmt.setString(1, inv.getName());
            this.pStmt.setDate(2, inv.getStartDate());
            this.pStmt.setDate(3, inv.getEndDate());
                
            status = this.pStmt.executeUpdate();
            if(status > 0)
                JOptionPane.showMessageDialog(c, "New record successfully added.", "Success !", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(DBcontrol.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(c, "Something went wrong.\n\nCheck whether your input is correct or not?\nIf yes, this must be some system errror." , "Exception Occured", JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
    
    public int editReservationStatus(Reservation res){
        String query = "UPDATE reservation SET status='"+res.getStatus()+"' where reservation_id='"+res.getId()+"';";
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public ArrayList<String> getEmails(String table,boolean haveClass){
        ArrayList<String> emails = new ArrayList<String>();
        try {
            Reservation reservation = new Reservation();
            if(haveClass)
                this.rs = this.stmt.executeQuery("SELECT * FROM "+table.toLowerCase()+" where joined_class='Yes'");
            else
                this.rs = this.stmt.executeQuery("SELECT * FROM "+table.toLowerCase());
            if(rs.next()){
                emails.add(rs.getString("email"));
                while(rs.next()){
                    emails.add(rs.getString("email"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emails;
    }
    
    public int registerPatronInClass(Component c,Patron user,String instructor){
        int status = 0;
        String id = "";
        try {
            String query = "INSERT INTO class_registrations (patron_id,name,email,instructor) VALUES (?,?,?,?)";
            this.pStmt = this.dbCon.prepareStatement(query);
            this.pStmt.setString(1, user.getId());
            this.pStmt.setString(2, user.getFirstName());
            this.pStmt.setString(3, user.getEmail());
            this.pStmt.setString(4, instructor);
                
            status = this.pStmt.executeUpdate();
            if(status > 0)
                JOptionPane.showMessageDialog(c, "New record successfully added.", "Success !", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(DBcontrol.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(c, "Something went wrong.\n\nCheck whether your input is correct or not?\nIf yes, this must be some system errror." , "Exception Occured", JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
    
    public int editPatronAccess(Patron user){
        String query = "UPDATE patron SET access='"+user.getAccess()+"' where patron_id='"+user.getId()+"';";
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public int editPatronSuspension(Patron user){
        String query = "UPDATE patron SET suspension_date='"+user.getSuspensionDate()+"',suspension_count='"+user.getSuspensionCount()+"',access='"+user.getAccess()+"' where patron_id='"+user.getId()+"';";
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public ArrayList<Report> getCheckinReport(int type,String from, String to){
        ArrayList<Report> reports = new ArrayList<Report>();
        try {
            Report report = new Report();
            if(type == 0){
                this.rs = this.stmt.executeQuery("select distinct user_type,patron_id, checkin_timestamp from checkin where user_type NOT IN ('Demographic') and checkin_timestamp between '"+from+"' and '"+to+"' GROUP BY patron_id ");
                //System.out.println("select distinct user_type,patron_id, checkin_timestamp from checkin where checkin_timestamp between '"+from+"' and '"+to+"' GROUP BY patron_id ");
                if(rs.next()){
                    report = new Report();
                    report.setType(rs.getString("user_type"));
                    report.setPatron(rs.getString("patron_id"));
                    report.setTimeStamp(rs.getDate("checkin_timestamp").toLocaleString());
                    reports.add(report);
                    while(rs.next()){
                        report = new Report();
                        report.setType(rs.getString("user_type"));
                        report.setPatron(rs.getString("patron_id"));
                        report.setTimeStamp(rs.getDate("checkin_timestamp").toLocaleString());
                        reports.add(report);
                    }
                }
                this.rs = this.stmt.executeQuery("select user_type,first_name, last_name,walvor,checkin_timestamp from checkin where user_type='Demographic' and checkin_timestamp between '"+from+"' and '"+to+"' ");
                //System.out.println("select user_type,first_name, last_name,walvor,checkin_timestamp from checkin where user_type='Demographic' and checkin_timestamp between '"+from+"' and '"+to+"' ");
                if(rs.next()){
                    report = new Report();
                    report.setType(rs.getString("user_type"));
                    report.setFirstName(rs.getString("first_name"));
                    report.setLastName(rs.getString("last_name"));
                    report.setWalver(rs.getString("walvor"));
                    report.setTimeStamp(rs.getDate("checkin_timestamp").toLocaleString());
                    reports.add(report);
                    while(rs.next()){
                        report = new Report();
                        report.setType(rs.getString("user_type"));
                        report.setFirstName(rs.getString("first_name"));
                        report.setLastName(rs.getString("last_name"));
                        report.setWalver(rs.getString("walvor"));
                        report.setTimeStamp(rs.getDate("checkin_timestamp").toLocaleString());
                        reports.add(report);
                    }
                }
            }
            else if(type == 1){
                this.rs = this.stmt.executeQuery("select user_type,first_name, last_name,walvor,checkin_timestamp from checkin where user_type='Demographic' and checkin_timestamp between '"+from+"' and '"+to+"' ");
                //System.out.println("select user_type,first_name, last_name,walvor,checkin_timestamp from checkin where user_type='Demographic' and checkin_timestamp between '"+from+"' and '"+to+"' ");
                if(rs.next()){
                    report = new Report();
                    report.setType(rs.getString("user_type"));
                    report.setFirstName(rs.getString("first_name"));
                    report.setLastName(rs.getString("last_name"));
                    report.setWalver(rs.getString("walvor"));
                    report.setTimeStamp(rs.getDate("checkin_timestamp").toLocaleString());
                    reports.add(report);
                    while(rs.next()){
                        report = new Report();
                        report.setType(rs.getString("user_type"));
                        report.setFirstName(rs.getString("first_name"));
                        report.setLastName(rs.getString("last_name"));
                        report.setWalver(rs.getString("walvor"));
                        report.setTimeStamp(rs.getDate("checkin_timestamp").toLocaleString());
                        reports.add(report);
                    }
                }
            }
            else if(type == 2){
                this.rs = this.stmt.executeQuery("SELECT * FROM `checkin` where checkin_timestamp between '"+from+"' and '"+to+"'");
                //System.out.println("SELECT * FROM `checkin` where checkin_timestamp between '"+from+"' and '"+to+"'");
                if(rs.next()){
                    report = new Report();
                    report.setType(rs.getString("user_type"));
                    report.setFirstName(rs.getString("first_name"));
                    report.setLastName(rs.getString("last_name"));
                    report.setWalver(rs.getString("walvor"));
                    report.setPatron(rs.getString("patron_id"));
                    report.setTimeStamp(rs.getDate("checkin_timestamp").toLocaleString());
                    reports.add(report);
                    while(rs.next()){
                        report = new Report();
                        report.setType(rs.getString("user_type"));
                        report.setFirstName(rs.getString("first_name"));
                        report.setLastName(rs.getString("last_name"));
                        report.setWalver(rs.getString("walvor"));
                        report.setPatron(rs.getString("patron_id"));
                        report.setTimeStamp(rs.getDate("checkin_timestamp").toLocaleString());
                        reports.add(report);
                    }
                }
            }
            else{
                this.rs = this.stmt.executeQuery("select distinct user_type,patron_id, checkin_timestamp from checkin where user_type='New' and checkin_timestamp between '"+from+"' and '"+to+"' GROUP BY patron_id  ");
                //System.out.println("select distinct user_type,patron_id, checkin_timestamp from checkin where user_type='New' and checkin_timestamp between '"+from+"' and '"+to+"' GROUP BY patron_id  ");
                if(rs.next()){
                    report = new Report();
                    report.setType(rs.getString("user_type"));
                    report.setPatron(rs.getString("patron_id"));
                    report.setTimeStamp(rs.getDate("checkin_timestamp").toLocaleString());
                    reports.add(report);
                    while(rs.next()){
                        report = new Report();
                        report.setType(rs.getString("user_type"));
                        report.setPatron(rs.getString("patron_id"));
                        report.setTimeStamp(rs.getDate("checkin_timestamp").toLocaleString());
                        reports.add(report);
                    }
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reports;
    }
    
    public int addClass(Component c,String name,String timefrom,String timeto,String datefrom,String dateto,String instructor){
        int status = 0;
        String id = "";
        try {
            String query = "INSERT INTO class (name,class_timing_start,class_timing_end,duration_start,duration_end,instructor) VALUES (?,?,?,?,?,?)";
            this.pStmt = this.dbCon.prepareStatement(query);
            this.pStmt.setString(1,name);
            this.pStmt.setString(2,timefrom);
            this.pStmt.setString(3,timeto);
            this.pStmt.setString(4,datefrom);
            this.pStmt.setString(5,dateto);
            this.pStmt.setString(6,instructor);
                
            status = this.pStmt.executeUpdate();
            if(status > 0)
                JOptionPane.showMessageDialog(c, "New record successfully added.", "Success !", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(DBcontrol.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(c, "Something went wrong.\n\nCheck whether your input is correct or not?\nIf yes, this must be some system errror." , "Exception Occured", JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
    
    public ArrayList<Classes> getClasses(){
        ArrayList<Classes> classes = new ArrayList<Classes>();
        try {
            Classes c = new Classes();
            this.rs = this.stmt.executeQuery("SELECT * FROM class");
            if(rs.next()){
                c = new Classes();
                c.setId((rs.getInt("class_id"))+"");
                c.setTimeFrom((rs.getString("class_timing_start")));
                c.setTimeTo((rs.getString("class_timing_end")));
                c.setDateFrom((rs.getString("duration_start")));
                c.setDateTo((rs.getString("duration_end")));
                c.setInstructor((rs.getString("instructor")));
                c.setName((rs.getString("name")));
                classes.add(c);
                while(rs.next()){
                    c = new Classes();
                    c.setId((rs.getInt("class_id"))+"");
                    c.setTimeFrom((rs.getString("class_timing_start")));
                    c.setTimeTo((rs.getString("class_timing_end")));
                    c.setDateFrom((rs.getString("duration_start")));
                    c.setDateTo((rs.getString("duration_end")));
                    c.setInstructor((rs.getString("instructor")));
                    c.setName((rs.getString("name")));
                    classes.add(c);
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return classes;
    }
    
    public int editClassSchedule(Classes classes){
        String query = "UPDATE class SET class_timing_start='"+classes.getTimeFrom()+"',class_timing_end='"+classes.getTimeTo()+"' where class_id='"+classes.getId()+"';";
        System.out.println(query);
        int affected = 0;
        try {
            affected = this.stmt.executeUpdate(query);
        } catch (SQLException ex) {
            
        }
        return affected;
    }
    
    public int addReservation(Component c,Reservation res){
        int status = 0;
        String id = "";
        try {
            String query = "INSERT INTO reservation (type,name,email,from_date,to_date,duration,description) VALUES (?,?,?,?,?,?,?)";
            this.pStmt = this.dbCon.prepareStatement(query);
            this.pStmt.setString(1, res.getType());
            this.pStmt.setString(2, res.getName());
            this.pStmt.setString(3, res.getEmail());
            this.pStmt.setString(4, res.getFrom().toString());
            this.pStmt.setString(5, res.getTo().toString());
            this.pStmt.setString(6, res.getDuration());
            this.pStmt.setString(7, res.getDescription());
            
            status = this.pStmt.executeUpdate();
            if(status > 0)
                JOptionPane.showMessageDialog(c, "New record successfully added.", "Success !", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(DBcontrol.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(c, "Something went wrong.\n\nCheck whether your input is correct or not?\nIf yes, this must be some system errror." , "Exception Occured", JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
}
