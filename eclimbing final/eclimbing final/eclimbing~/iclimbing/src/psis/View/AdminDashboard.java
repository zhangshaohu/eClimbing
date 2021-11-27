/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package psis.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import psis.Controller.DBcontrol;
import psis.Controller.SendEmail;
import psis.Controller.UserController;
import psis.Model.Employee;
import psis.Model.Inventory;
import psis.Model.Patron;
import psis.Model.Report;
import psis.Model.Reservation;
import static psis.View.RegisterView.VALID_DATE_REGEX;
import static psis.View.RegisterView.VALID_EMAIL_ADDRESS_REGEX;
import static psis.View.RegisterView.validateDate;
import psis.utils.Fields;


public class AdminDashboard extends javax.swing.JFrame {
    public static final Pattern VALID_TIME_REGEX = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]", Pattern.CASE_INSENSITIVE);
    
    DBcontrol db;
    Patron patron;
    Employee employee;
    Inventory inventory;
    /**
     * Creates new form Form
     */
    public AdminDashboard() {
        initComponents();
    }
    
    void showTabularDialog(String title, Object[] header, Object[][] data){

        final JDialog dialog = new JDialog(this, title, true);
        Container contentPane = dialog.getContentPane();
        JScrollPane jScrolPn = new JScrollPane();
        javax.swing.JTable jTbl = new javax.swing.JTable();

        jScrolPn.setViewportView(jTbl);
        DefaultTableModel model = (DefaultTableModel) jTbl.getModel();
        model.setRowCount(0);

        for(int i=0; i< header.length; i++){
            model.addColumn(header[i]);
        }

        for(int i=0; i<data.length; i++){
            model.addRow(data[i]);
        }

        contentPane.add(jScrolPn, BorderLayout.LINE_START);
        dialog.pack();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);   //  Shows PatronDashboard in the center of the screen
        dialog.setVisible(true);
    }
    
    /**
     * 
     * @param model - the object which is invoking the method
     * @param action - action to be performed e.g. Add, Edit, Delete
     * @param attributes - object's fields to be shown to the user
     */
    
    void refreshAllTables(){
        loadStaffTable();
        loadPatronTable();
        loadInventoryTable();
        loadReservationTable();
    }
    
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    
    public static boolean validateDate(String dateStr) {
        Matcher matcher = VALID_DATE_REGEX .matcher(dateStr);
        return matcher.find();
    }
    
    public static boolean validateTime(String timeStr) {
        Matcher matcher = VALID_TIME_REGEX .matcher(timeStr);
        return matcher.find();
    }

    
    void crud(final String model, final String action, final String[] fields){
        final String[] fieldValues = new String[fields.length];
       
        final JLabel[] labels = new JLabel[fields.length];
        final JTextField[] txtFlds = new JTextField[fields.length];
        
        final JDialog dialog = new JDialog(this, action.toUpperCase()+" "+model.toUpperCase(), true);
        Container contentPane = dialog.getContentPane();
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        for(int i=0; i<fields.length; i++){
            labels[i] = new JLabel(fields[i]);
            panel.add(labels[i]);
            
            txtFlds[i] = new JTextField();
            txtFlds[i].setColumns(15);
            panel.add(txtFlds[i]);
            
            if (action.equalsIgnoreCase("edit") && model.equalsIgnoreCase("patron") ){
                txtFlds[i].setText(patron.get(i));
            }
            else if (action.equalsIgnoreCase("edit") && model.equalsIgnoreCase("employee") ){
                txtFlds[i].setText(employee.get(i));
            }
        }        
        panel.setPreferredSize(new Dimension(200, 60 * fields.length));
        contentPane.add(panel, BorderLayout.LINE_START);
        
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton clearBtn = new JButton("CLEAR");
        JButton actionBtn = new JButton(action.toUpperCase());
        panelBtns.add(clearBtn);
        panelBtns.add(actionBtn);
        
        panelBtns.setPreferredSize(new Dimension(200, 40));
        contentPane.add(panelBtns, BorderLayout.PAGE_END);
        
        clearBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for(int i = 0; i<fields.length; i++){
                    txtFlds[i].setText("");
                }
            }
        });
        
        actionBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for(int i = 0; i<fields.length; i++){
                    fieldValues[i] = txtFlds[i].getText();
//                    System.out.println(fieldValues[i]);
                }
                if(action.equalsIgnoreCase("add")){
                    add(model,fields,fieldValues);
                } else if (action.equalsIgnoreCase("edit")){
                    edit(model,fields,fieldValues);
                } else if (action.equalsIgnoreCase("delete")){
                    dialog.dispose();
                    delete(model,fields,fieldValues);
                } else if (action.equalsIgnoreCase("find")){
                    dialog.dispose();
                    find(model,fields,fieldValues);
                }
                 else if (action.equalsIgnoreCase("accept")){
                    dialog.dispose();
                    add(model,fields,fieldValues);
                }
                 else if (action.equalsIgnoreCase("reject")){
                    dialog.dispose();
                    delete(model,fields,fieldValues);
                }
                
            }
        });
        
        dialog.pack();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);   //  Shows PatronDashboard in the center of the screen
        dialog.setVisible(true);
    }
    
    void delete(String model, String[] fields, String []values){
        HashMap<String, String> map = new HashMap<String, String>();
        for(int i=0; i<fields.length; i++){
            map.put(fields[i], values[i]);
        }
        int result = 0 ;
        switch (model){
            case "employee":
                UserController login = new UserController(this.db);
                if( login.deleteEmployee(map.get(Fields.employeeID())) > 0 ) this.loadStaffTable();
                break;
            case "patron":
                login = new UserController(this.db);
                if( login.deletePatron(map.get(Fields.patronID())) > 0 ) this.loadPatronTable();
                break;
            case "inventory":
                login = new UserController(this.db);
                if( login.deleteInventory(map.get(Fields.inventoryID())) > 0 ) this.loadInventoryTable();
                break;
            case "reservation":
                String id = map.get("Reservation ID");
                Reservation res = new Reservation();
                res.setId(Integer.parseInt(id));
                res.setStatus("Rejected");
                login = new UserController(this.db);
                login = new UserController(this.db);
                
                if( login.editReservationStatus(res) > 0 ){
                    this.loadReservationTable();
                    SwingWorker worker = new SwingWorker<Void, Void>() {
                        @Override
                        public Void doInBackground() {
                            SendEmail.send(res.getEmail(), "Reservation request is rejected.", "Details:\n\n"+res.getDescription());
                            return null;
                        }
                        @Override
                        public void done() {
                        }
                    };
                    worker.execute();
                }
                break;
        }
    }
    boolean afterDeleteAction(String model, int result){
        if (result>0){
            JOptionPane.showMessageDialog(this, result+" record successfully deleted.", "Success !", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        JOptionPane.showMessageDialog(this, "It doesn't seem to be present in your system.", "Record is not deleted", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    
    void add(String model, String[] fields, String []values){
        HashMap<String, String> map = new HashMap<String, String>();
        for(int i=0; i<fields.length; i++){
            map.put(fields[i], values[i]);
        }
        switch (model){
            case "reservation":
                String id = map.get("Reservation ID");
                Reservation res = new Reservation();
                res.setId(Integer.parseInt(id));
                res.setStatus("Accepted");
                UserController login = new UserController(this.db);
                login = new UserController(this.db);
                
                if( login.editReservationStatus(res) > 0 ) {
                    this.loadReservationTable();
                    SwingWorker worker = new SwingWorker<Void, Void>() {
                        @Override
                        public Void doInBackground() {
                            SendEmail.send(res.getEmail(), "Reservation request is accepted", "Details:\n\n"+res.getDescription());
                            return null;
                        }
                        @Override
                        public void done() {
                        }
                    };
                    worker.execute();
                }
                break;
            case "employee":
                String firstname = map.get(Fields.patronFirstName());
                String lastname = map.get(Fields.patronLastName());
                String username = map.get(Fields.patronUserName());
                String password = map.get(Fields.patronPassword());
                String confirm_password = map.get(Fields.patronPassword());
                String email = map.get(Fields.patronEmail());
                String phone = map.get(Fields.patronPhone());
                String dob = map.get(Fields.patronDOB());
                String sex = map.get(Fields.patronSex());

                if(firstname.length() < 4){
                    JOptionPane.showMessageDialog(rootPane, "Enter first name at least 4 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(lastname.length() < 4){
                    JOptionPane.showMessageDialog(rootPane, "Enter last name at least 4 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(username.length() < 8){
                    JOptionPane.showMessageDialog(rootPane, "Enter username at least 8 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(password.length() < 7 || password.length() > 15 ){
                    JOptionPane.showMessageDialog(rootPane, "Enter password with 7 -15 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(confirm_password.length() < 7 || confirm_password.length() > 15 ){
                    JOptionPane.showMessageDialog(rootPane, "Enter confirm password with 7 -15 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!password.equals(confirm_password)){
                    JOptionPane.showMessageDialog(rootPane, "password and confirm password do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!this.validate(email)) {
                    JOptionPane.showMessageDialog(rootPane, "Enter valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(phone.length() < 10){
                    JOptionPane.showMessageDialog(rootPane, "Enter phone number at least 10 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!validateDate(dob)){
                    JOptionPane.showMessageDialog(rootPane, "Enter Date Of Birth in valid format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Employee emp = new Employee();
                emp.setFirstName(firstname);
                emp.setLastName(lastname);
                emp.setEmail(email);
                emp.setPassword(password);
                emp.setDOB(Date.valueOf(dob));
                emp.setUserName(username);
                emp.setSex(sex);
                emp.setPhone(phone);

                login = new UserController(this.db);
                login = new UserController(this.db);
                
                if( login.addEmployee(this,emp) > 0 ) this.loadStaffTable();
                break;
            case "patron":
                firstname = map.get(Fields.patronFirstName());
                lastname = map.get(Fields.patronLastName());
                username = map.get(Fields.patronUserName());
                password = map.get(Fields.patronPassword());
                confirm_password = map.get(Fields.patronPassword());
                email = map.get(Fields.patronEmail());
                phone = map.get(Fields.patronPhone());
                dob = map.get(Fields.patronDOB());
                sex = map.get(Fields.patronSex());
                String payment = map.get(Fields.patronPayment());
                String semester = map.get(Fields.patronSemester());

                if(firstname.length() < 4){
                    JOptionPane.showMessageDialog(rootPane, "Enter first name at least 4 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(lastname.length() < 4){
                    JOptionPane.showMessageDialog(rootPane, "Enter last name at least 4 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(username.length() < 8){
                    JOptionPane.showMessageDialog(rootPane, "Enter username at least 8 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(password.length() < 7 || password.length() > 15 ){
                    JOptionPane.showMessageDialog(rootPane, "Enter password with 7 -15 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(confirm_password.length() < 7 || confirm_password.length() > 15 ){
                    JOptionPane.showMessageDialog(rootPane, "Enter confirm password with 7 -15 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!password.equals(confirm_password)){
                    JOptionPane.showMessageDialog(rootPane, "password and confirm password do not match.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!this.validate(email)) {
                    JOptionPane.showMessageDialog(rootPane, "Enter valid email address.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(phone.length() < 10){
                    JOptionPane.showMessageDialog(rootPane, "Enter phone number at least 10 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!validateDate(dob)){
                    JOptionPane.showMessageDialog(rootPane, "Enter Date Of Birth in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Patron user = new Patron();
                user.setFirstName(firstname);
                user.setLastName(lastname);
                user.setSemester(semester);
                user.setEmail(email);
                user.setPassword(password);
                user.setDOB(Date.valueOf(dob));
                user.setUserName(username);
                user.setSex(sex);
                user.setPhone(phone);
                user.setPayment(payment);

                login = new UserController(this.db);
                
                if( login.addPatron(this,user) > 0 ) this.loadPatronTable();
                break;
            case "inventory":
                String name = map.get(Fields.inventoryName());
                String start_date = map.get(Fields.inventoryStartDate());
                String end_date = map.get(Fields.inventoryEndDate());
                
                if(name.length() < 4){
                    JOptionPane.showMessageDialog(rootPane, "Enter Name at least 4 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!validateDate(start_date)){
                    JOptionPane.showMessageDialog(rootPane, "Enter Start Date in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!validateDate(end_date)){
                    JOptionPane.showMessageDialog(rootPane, "Enter End Date in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Inventory inv = new Inventory();
                inv.setName(name);
                inv.setStartDate(Date.valueOf(start_date));
                inv.setEndDate(Date.valueOf(end_date));
                
                login = new UserController(this.db);
                
                if( login.addInventory(this,inv) > 0 ) this.loadInventoryTable();
                
                break;
        }
    }
    
    void edit(String model, String[] fields, String []values){
        HashMap<String, String> map = new HashMap<String, String>();
        for(int i=0; i<fields.length; i++){
            map.put(fields[i], values[i]);
        }
        int result = 0 ;
        switch (model){
            case "employee":
                String firstname = map.get(Fields.patronFirstName());
                String lastname = map.get(Fields.patronLastName());
                String username = map.get(Fields.patronUserName());
                String password = map.get(Fields.patronPassword());
                String confirm_password = map.get(Fields.patronPassword());
                String email = map.get(Fields.patronEmail());
                String phone = map.get(Fields.patronPhone());
                String dob = map.get(Fields.patronDOB());
                String sex = map.get(Fields.patronSex());

                if(firstname.length() < 4){
                    JOptionPane.showMessageDialog(rootPane, "Enter first name at least 4 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(lastname.length() < 4){
                    JOptionPane.showMessageDialog(rootPane, "Enter last name at least 4 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(username.length() < 8){
                    JOptionPane.showMessageDialog(rootPane, "Enter username at least 8 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(password.length() < 7 || password.length() > 15 ){
                    JOptionPane.showMessageDialog(rootPane, "Enter password with 7 -15 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(confirm_password.length() < 7 || confirm_password.length() > 15 ){
                    JOptionPane.showMessageDialog(rootPane, "Enter confirm password with 7 -15 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!password.equals(confirm_password)){
                    JOptionPane.showMessageDialog(rootPane, "password and confirm password do not match.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!this.validate(email)) {
                    JOptionPane.showMessageDialog(rootPane, "Enter valid email address.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(phone.length() < 10){
                    JOptionPane.showMessageDialog(rootPane, "Enter phone number at least 10 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!validateDate(dob)){
                    JOptionPane.showMessageDialog(rootPane, "Enter Date Of Birth in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Employee emp = new Employee();
                emp.setFirstName(firstname);
                emp.setLastName(lastname);
                emp.setEmail(email);
                emp.setPassword(password);
                emp.setDOB(Date.valueOf(dob));
                emp.setUserName(username);
                emp.setSex(sex);
                emp.setPhone(phone);

                UserController login = new UserController(this.db);
                result = login.editEmployee(emp);
                if(this.afterEditAction(model,result)) this.loadStaffTable();
                break;
            case "patron":
                firstname = map.get(Fields.patronFirstName());
                lastname = map.get(Fields.patronLastName());
                username = map.get(Fields.patronUserName());
                password = map.get(Fields.patronPassword());
                confirm_password = map.get(Fields.patronPassword());
                email = map.get(Fields.patronEmail());
                phone = map.get(Fields.patronPhone());
                dob = map.get(Fields.patronDOB());
                sex = map.get(Fields.patronSex());
                String semester = map.get(Fields.patronSemester());
                String payment = map.get(Fields.patronPayment());
                
                if(firstname.length() < 4){
                    JOptionPane.showMessageDialog(rootPane, "Enter first name at least 4 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(lastname.length() < 4){
                    JOptionPane.showMessageDialog(rootPane, "Enter last name at least 4 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(username.length() < 8){
                    JOptionPane.showMessageDialog(rootPane, "Enter username at least 8 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(password.length() < 7 || password.length() > 15 ){
                    JOptionPane.showMessageDialog(rootPane, "Enter password with 7 -15 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(confirm_password.length() < 7 || confirm_password.length() > 15 ){
                    JOptionPane.showMessageDialog(rootPane, "Enter confirm password with 7 -15 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!password.equals(confirm_password)){
                    JOptionPane.showMessageDialog(rootPane, "password and confirm password do not match.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!this.validate(email)) {
                    JOptionPane.showMessageDialog(rootPane, "Enter valid email address.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(phone.length() < 10){
                    JOptionPane.showMessageDialog(rootPane, "Enter phone number at least 10 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(!validateDate(dob)){
                    JOptionPane.showMessageDialog(rootPane, "Enter Date Of Birth in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Patron user = new Patron();
                user.setFirstName(firstname);
                user.setLastName(lastname);
                user.setSemester(semester);
                user.setEmail(email);
                user.setPassword(password);
                user.setDOB(Date.valueOf(dob));
                user.setUserName(username);
                user.setSex(sex);
                user.setPhone(phone);
                user.setPayment(payment);
                
                login = new UserController(this.db);
                result = login.editPatron(user);
                if(this.afterEditAction(model,result)) this.loadPatronTable();
                break;
        }
    }
    
    void find(String model, String[] fields, String []values){
        HashMap<String, String> map = new HashMap<String, String>();
        for(int i=0; i<fields.length; i++){
            map.put(fields[i], values[i]);
        }
        int result = 0 ;
        switch (model){
            case "employee":
                String id = map.get(Fields.employeeID());
                Employee emp = new Employee();
                emp.setId(id);
                UserController login = new UserController(this.db);
                emp  = login.getEmployee(emp);
                employee = emp;
                System.out.println("\n\n\nID : "+employee.getId()+"\n\n");
                if(emp.getAccess() != null || !emp.getAccess().equals("error")){
                    this.crud("employee", "edit", Fields.employeeFields());
                }
                else{
                    if(this.afterEditAction(model,result)) this.loadStaffTable();
                }
                break;
            case "patron":
                id = map.get(Fields.patronID());
                Patron user = new Patron();
                user.setUserName(id);
                login = new UserController(this.db);
                user  = login.getPatron(user);
                patron = user;
                if(!user.getAccess().equals("error")){
                    this.crud("patron", "edit", Fields.patronFields());
                }
                else{
                    if(this.afterEditAction(model,result)) this.loadPatronTable();
                }
                break;
        }
    }

        
    boolean afterEditAction(String model, int result){
        if (result>0){
            JOptionPane.showMessageDialog(this, result+" record successfully edited.", "Success !", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        JOptionPane.showMessageDialog(this, "It doesn't seem to be present in your system.", "Record is not edited", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    
    void loadStaffTable(){
        UserController userController = new UserController(this.db);
        ArrayList<Employee> allEmployees = userController.getAllEmployee();
        
        DefaultTableModel model = (DefaultTableModel) this.jTbl_drugs.getModel();
        model.setRowCount(0);
        
        for(int r=0; r<allEmployees.size(); r++){
            Object[] row = { allEmployees.get(r).getId(), allEmployees.get(r).getFirstName(), allEmployees.get(r).getSex(), allEmployees.get(r).getEmail(), allEmployees.get(r).getPhone()};
            model.addRow(row);
        }
    }
    
    void loadPatronTable(){
        UserController userController = new UserController(this.db);
        ArrayList<Patron> allPatrons = userController.getAllPatrons();
        
        DefaultTableModel model = (DefaultTableModel) this.jTbl_drugs2.getModel();
        model.setRowCount(0);
        
        for(int r=0; r<allPatrons.size(); r++){
            Object[] row = { allPatrons.get(r).getId(), allPatrons.get(r).getFirstName(), allPatrons.get(r).getSex(), allPatrons.get(r).getEmail(), allPatrons.get(r).getPhone()};
            model.addRow(row);
        }
    }
    
    void loadInventoryTable(){
        UserController userController = new UserController(this.db);
        ArrayList<Inventory> allInventories = userController.getAllInventories();
        
        DefaultTableModel model = (DefaultTableModel) this.jTbl_drugs4.getModel();
        model.setRowCount(0);
        
        for(int i=0; i<allInventories.size(); i++){
            Object[] row = { allInventories.get(i).getId(), allInventories.get(i).getName(), allInventories.get(i).getStartDate().toLocaleString(),allInventories.get(i).getEndDate().toLocaleString()};
            model.addRow(row);
            
            Calendar c = Calendar.getInstance();
            Date now = new Date(c.getTime().getTime());
            Date old = allInventories.get(i).getEndDate();
            double diff = getDateDiff(now,old,TimeUnit.DAYS);
            
            if(diff <= 7){
                final String id = allInventories.get(i).getId()+"";
                SwingWorker worker = new SwingWorker<Void, Void>() {
                    @Override
                    public Void doInBackground() {
                        SendEmail.send("hello.eclimbing@gmail.com", "Inventory Expiry Notification", "Inventory with id "+id+" has expiray date in less than 7 days.");
                        return null;
                    }
                    @Override
                    public void done() {
                    }
                };
                worker.execute();
            }
            
        }
    }
    
    void loadReservationTable(){
        UserController userController = new UserController(this.db);
        ArrayList<Reservation> allReservations = userController.getAllReservations();
        
        DefaultTableModel model = (DefaultTableModel) this.jTbl_drugs3.getModel();
        model.setRowCount(0);
        
        for(int i=0; i<allReservations.size(); i++){
            Object[] row = { allReservations.get(i).getId(), allReservations.get(i).getType(), allReservations.get(i).getName(),allReservations.get(i).getFrom().toLocaleString(),allReservations.get(i).getTo().toLocaleString(),allReservations.get(i).getDuration(),allReservations.get(i).getDescription(),allReservations.get(i).getStatus()};
            model.addRow(row);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
 regenerated by the PatronDashboard Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JTabbedPane TabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btn_editDrug = new javax.swing.JButton();
        btn_newDrug = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTbl_drugs = new javax.swing.JTable();
        btn_deleteDrug1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        btn_newDrug2 = new javax.swing.JButton();
        btn_editDrug2 = new javax.swing.JButton();
        btn_deleteDrug4 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTbl_drugs2 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        btn_newDrug4 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTbl_drugs4 = new javax.swing.JTable();
        btn_deleteDrug7 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btn_newDrug3 = new javax.swing.JButton();
        btn_editDrug3 = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTbl_drugs3 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        TabbedPane.setToolTipText("");
        TabbedPane.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel9.setText("Create Class");

        jLabel1.setText("Class Name:");

        jTextField1.setToolTipText("");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Class Timings:");

        jTextField2.setToolTipText("");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel8.setText("Class Duration:");

        jLabel27.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel27.setText("Format (HH-MM)");

        jTextField4.setToolTipText("");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel32.setText("Format (HH-MM)");

        jLabel10.setText("Class Instructor:");

        jTextField5.setToolTipText("");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jTextField6.setToolTipText("");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel33.setText("Format (YYYY-MM-DD)");

        jLabel34.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel34.setText("Format (YYYY-MM-DD)");

        jButton1.setText("Create");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setText("To");

        jLabel11.setText("To");

        jTextField3.setToolTipText("");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addComponent(jLabel33)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(26, 26, 26)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel9))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(101, 101, 101)
                            .addComponent(jLabel1)
                            .addGap(26, 26, 26)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel27))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel34)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel32))
                .addContainerGap(607, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel34)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addComponent(jButton1)
                .addContainerGap(182, Short.MAX_VALUE))
        );

        TabbedPane.addTab("Create Class", jPanel1);

        jLabel4.setText("Suspend/Renew account for patron.");

        jLabel16.setText("Enter Patron ID:");

        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });

        jButton7.setText("Suspend");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Renew");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton8MouseClicked(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(296, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(631, 631, 631))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addGap(0, 310, Short.MAX_VALUE))
        );

        TabbedPane.addTab("Suspend/Renew", jPanel2);

        jLabel2.setText("Staff Management");

        btn_editDrug.setText("Edit Employee");
        btn_editDrug.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_editDrugMouseClicked(evt);
            }
        });
        btn_editDrug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editDrugActionPerformed(evt);
            }
        });

        btn_newDrug.setText("New Employee");
        btn_newDrug.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_newDrugMouseClicked(evt);
            }
        });
        btn_newDrug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newDrugActionPerformed(evt);
            }
        });

        jTbl_drugs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "SEX", "Email", "Phone"
            }
        ));
        jScrollPane6.setViewportView(jTbl_drugs);

        btn_deleteDrug1.setText("Delete Employee");
        btn_deleteDrug1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_deleteDrug1MouseClicked(evt);
            }
        });
        btn_deleteDrug1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteDrug1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1174, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btn_newDrug)
                        .addGap(18, 18, 18)
                        .addComponent(btn_editDrug)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_deleteDrug1)))
                .addContainerGap(655, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_newDrug)
                    .addComponent(btn_editDrug)
                    .addComponent(btn_deleteDrug1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addGap(29, 29, 29))
        );

        TabbedPane.addTab("Staff", jPanel3);

        jLabel19.setText("Patron Management");

        btn_newDrug2.setText("New Patron");
        btn_newDrug2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_newDrug2MouseClicked(evt);
            }
        });
        btn_newDrug2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newDrug2ActionPerformed(evt);
            }
        });

        btn_editDrug2.setText("Edit Patron");
        btn_editDrug2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_editDrug2MouseClicked(evt);
            }
        });
        btn_editDrug2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editDrug2ActionPerformed(evt);
            }
        });

        btn_deleteDrug4.setText("Delete Patron");
        btn_deleteDrug4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_deleteDrug4MouseClicked(evt);
            }
        });
        btn_deleteDrug4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteDrug4ActionPerformed(evt);
            }
        });

        jTbl_drugs2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "SEX", "Email", "Phone"
            }
        ));
        jScrollPane8.setViewportView(jTbl_drugs2);
        if (jTbl_drugs2.getColumnModel().getColumnCount() > 0) {
            jTbl_drugs2.getColumnModel().getColumn(4).setHeaderValue("Phone");
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1174, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel19))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btn_newDrug2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_editDrug2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_deleteDrug4)))
                .addContainerGap(733, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_newDrug2)
                    .addComponent(btn_editDrug2)
                    .addComponent(btn_deleteDrug4))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addGap(29, 29, 29))
        );

        TabbedPane.addTab("Patron", jPanel6);

        btn_newDrug4.setText("New Item");
        btn_newDrug4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_newDrug4MouseClicked(evt);
            }
        });
        btn_newDrug4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newDrug4ActionPerformed(evt);
            }
        });

        jTbl_drugs4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Start", "End"
            }
        ));
        jScrollPane10.setViewportView(jTbl_drugs4);

        btn_deleteDrug7.setText("Delete Item");
        btn_deleteDrug7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_deleteDrug7MouseClicked(evt);
            }
        });
        btn_deleteDrug7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteDrug7ActionPerformed(evt);
            }
        });

        jLabel21.setText("Inventory Management");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addComponent(btn_deleteDrug7)))
                .addContainerGap(837, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 1162, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addComponent(btn_newDrug4)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_deleteDrug7)
                .addContainerGap(407, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(btn_newDrug4)
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                    .addGap(35, 35, 35)))
        );

        TabbedPane.addTab("Inventory", jPanel9);

        jLabel6.setText("Reservation Management");

        btn_newDrug3.setText("Accept");
        btn_newDrug3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_newDrug3MouseClicked(evt);
            }
        });
        btn_newDrug3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newDrug3ActionPerformed(evt);
            }
        });

        btn_editDrug3.setText("Reject");
        btn_editDrug3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_editDrug3MouseClicked(evt);
            }
        });
        btn_editDrug3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editDrug3ActionPerformed(evt);
            }
        });

        jTbl_drugs3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "User Type", "Name", "From", "To", "Duration", "Description", "Status"
            }
        ));
        jScrollPane9.setViewportView(jTbl_drugs3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(937, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1162, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(btn_newDrug3)
                            .addGap(18, 18, 18)
                            .addComponent(btn_editDrug3)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(448, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(51, 51, 51)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_newDrug3)
                        .addComponent(btn_editDrug3))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                    .addGap(18, 18, 18)))
        );

        TabbedPane.addTab("Reservation", jPanel4);

        jLabel3.setText("Lister For Patrons/Employees.");

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jScrollPane4.setViewportView(jTextArea4);

        jLabel13.setText("Note:");

        jButton5.setText("Send");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Patron", "Employee" }));

        jLabel14.setText("Send To:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(671, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addContainerGap(113, Short.MAX_VALUE))
        );

        TabbedPane.addTab("Lister", jPanel5);

        jLabel12.setText("Generate Reports in CSV Format");

        jLabel15.setText("Select report Type:");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Unique Visitors", "Demographic Visitors", "Total Participation", "New Visitors" }));

        jLabel17.setText("Select Duration:");

        jTextField7.setToolTipText("");
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel35.setText("Format (YYYY-MM-DD)");

        jLabel18.setText("To");

        jTextField8.setToolTipText("");
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        jLabel36.setText("Format (YYYY-MM-DD)");

        jButton2.setText("Generate");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel35)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel36)))
                            .addComponent(jButton2)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12)))
                .addGap(0, 681, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(34, 34, 34)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(289, Short.MAX_VALUE))
        );

        TabbedPane.addTab("Report Generation", jPanel7);

        jLabel20.setText("Do you really want to logout?");

        jButton3.setText("Yes");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(903, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jButton3))
                .addContainerGap(435, Short.MAX_VALUE))
        );

        TabbedPane.addTab("Logout", jPanel8);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabbedPane)
                .addContainerGap())
        );

        TabbedPane.getAccessibleContext().setAccessibleName("Personal Profile");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btn_editDrugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editDrugActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btn_editDrugActionPerformed

    private void btn_newDrugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newDrugActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btn_newDrugActionPerformed

    private void btn_deleteDrug1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteDrug1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_deleteDrug1ActionPerformed

    private void btn_newDrug2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newDrug2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_newDrug2ActionPerformed

    private void btn_editDrug2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editDrug2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_editDrug2ActionPerformed

    private void btn_deleteDrug4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteDrug4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_deleteDrug4ActionPerformed

    private void btn_newDrug3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newDrug3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_newDrug3ActionPerformed

    private void btn_editDrug3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editDrug3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_editDrug3ActionPerformed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
        this.dispose();
        this.setVisible(false);
        LoginView checkin = new LoginView();
        checkin.main(db);
    }//GEN-LAST:event_jButton3MouseClicked

    private void btn_newDrug4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newDrug4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_newDrug4ActionPerformed

    private void btn_deleteDrug7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteDrug7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_deleteDrug7ActionPerformed

    private void btn_newDrugMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_newDrugMouseClicked
        // TODO add your handling code here:
        this.crud("employee", "add", Fields.employeeFieldsWithoutID());
    }//GEN-LAST:event_btn_newDrugMouseClicked

    private void btn_editDrugMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_editDrugMouseClicked
        // TODO add your handling code here:
        String[] strArray = new String[] {Fields.employeeID()};
        this.crud("employee", "find", strArray);
    }//GEN-LAST:event_btn_editDrugMouseClicked

    private void btn_deleteDrug1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteDrug1MouseClicked
        // TODO add your handling code here:
        String[] strArray = new String[] {Fields.employeeID()};
        this.crud("employee", "delete", strArray);
    }//GEN-LAST:event_btn_deleteDrug1MouseClicked

    private void btn_newDrug2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_newDrug2MouseClicked
        // TODO add your handling code here:
        this.crud("patron", "add", Fields.patronFieldsWithoutID());
    }//GEN-LAST:event_btn_newDrug2MouseClicked

    private void btn_editDrug2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_editDrug2MouseClicked
        // TODO add your handling code here:
        String[] strArray = new String[] {Fields.patronID()};
        this.crud("patron", "find", strArray);
    }//GEN-LAST:event_btn_editDrug2MouseClicked

    private void btn_deleteDrug4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteDrug4MouseClicked
        // TODO add your handling code here:
        String[] strArray = new String[] {Fields.patronID()};
        this.crud("patron", "delete", strArray);
    }//GEN-LAST:event_btn_deleteDrug4MouseClicked

    private void btn_newDrug4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_newDrug4MouseClicked
        // TODO add your handling code here:
        this.crud("inventory", "add", Fields.inventoryFields());
    }//GEN-LAST:event_btn_newDrug4MouseClicked

    private void btn_deleteDrug7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_deleteDrug7MouseClicked
        // TODO add your handling code here:
        String[] strArray = new String[] {Fields.inventoryID()};
        this.crud("inventory", "delete", strArray);
    }//GEN-LAST:event_btn_deleteDrug7MouseClicked

    private void btn_newDrug3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_newDrug3MouseClicked
        // TODO add your handling code here:
        String[] strArray = new String[] {"Reservation ID"};
        this.crud("reservation", "accept", strArray);
    }//GEN-LAST:event_btn_newDrug3MouseClicked

    private void btn_editDrug3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_editDrug3MouseClicked
        // TODO add your handling code here:
        String[] strArray = new String[] {"Reservation ID"};
        this.crud("reservation", "reject", strArray);
    }//GEN-LAST:event_btn_editDrug3MouseClicked
    
    
    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // TODO add your handling code here:
        
        String note = jTextArea4.getText();
        String table = jComboBox1.getSelectedItem().toString();
        if(note.length() < 20){
            JOptionPane.showMessageDialog(rootPane, "Enter Note minimum 20 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UserController user = new UserController(this.db);
        ArrayList<String> emails = user.getEmails(table, false);
        
        jTextArea4.setText("");
                
        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                for (int i = 0; i < emails.size(); i++) {
                    SendEmail.send(emails.get(i), "Lister By Admin", "Note:\n\n"+note);
                }
                return null;
            }
            @Override
            public void done() {
                JOptionPane.showMessageDialog(rootPane, "Email sent to "+table+"s regarding lister.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        worker.execute();
        
    }//GEN-LAST:event_jButton5MouseClicked

    /** Using Calendar - THE CORRECT WAY**/  
    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
    
    private void jButton8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseClicked
        // TODO add your handling code here:
        String id = jTextField12.getText().toString();

        if(id.length() < 6 || id.length() > 6 ){
            JOptionPane.showMessageDialog(rootPane, "Enter Patron ID exactly 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Patron user = new Patron();
        user.setUserName(id);
        UserController userController = new UserController(this.db);
        user = userController.getPatron(user);
        
        if(user.getSuspensionCount() > 0){
            if(user.getSuspensionCount() >= 3){
                JOptionPane.showMessageDialog(rootPane, "Patron's suspension count is greater than two. This account cannot be renewed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                Calendar c = Calendar.getInstance();
                Date now = new Date(c.getTime().getTime());
                Date old = user.getSuspensionDate();
                double diff = getDateDiff(old,now,TimeUnit.DAYS);
                    
                if(user.getSuspensionCount() == 1){
                    if(diff < 7){
                        JOptionPane.showMessageDialog(rootPane, "Patron's suspension period is not completed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        user.setAccess("Yes");
                        userController.editPatronAccess(user);
                        if(userController.editPatronAccess(user) > 0)
                            JOptionPane.showMessageDialog(rootPane, "Patron's suspension is now removed.", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else{
                    if(diff < 30){
                        JOptionPane.showMessageDialog(rootPane, "Patron's suspension period is not completed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        user.setAccess("Yes");
                        if(userController.editPatronAccess(user) > 0)
                            JOptionPane.showMessageDialog(rootPane, "Patron's suspension is now removed.", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Patron's suspension count is already zero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton8MouseClicked

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
        // TODO add your handling code here:
        String id = jTextField12.getText().toString();

        if(id.length() < 6 || id.length() > 6 ){
            JOptionPane.showMessageDialog(rootPane, "Enter Patron ID exactly 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Patron user = new Patron();
        user.setUserName(id);
        UserController userController = new UserController(this.db);
        user = userController.getPatron(user);
        user.setSuspensionCount(user.getSuspensionCount()+1);
        
        Calendar c = Calendar.getInstance();
        user.setSuspensionDate(new Date(c.getTime().getTime()));
        user.setAccess("denied");
        
        if(userController.editPatronSuspension(user) > 0)
            JOptionPane.showMessageDialog(rootPane, "Patron is now put on suspension.", "Success", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(rootPane, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
            
    }//GEN-LAST:event_jButton7MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        int type = jComboBox3.getSelectedIndex();
        String from = jTextField7.getText();
        String to = jTextField8.getText();
        
        if(!validateDate(from)){
            JOptionPane.showMessageDialog(rootPane, "Enter From Date in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!validateDate(to)){
            JOptionPane.showMessageDialog(rootPane, "Enter To Date in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UserController userController = new UserController(this.db);
        ArrayList<Report> reports = userController.getCheckinReport(type,from,to);
        
        System.out.println(reports.size());
        
        PrintWriter pw;
        try {
            pw = new PrintWriter(new File("C:\\Users\\appala.chekuri\\Desktop\\project"+jComboBox3.getSelectedItem().toString()+".csv"));
            StringBuilder sb = new StringBuilder();
            sb.append("Timestamp");
            sb.append(',');
            sb.append("User Type");
            sb.append(',');
            sb.append("Patron ID");
            sb.append(',');
            sb.append("First Name");
            sb.append(',');
            sb.append("Last Name");
            sb.append(',');
            sb.append("Walver");
            sb.append('\n');

            for (int i = 0; i < reports.size(); i++) {
                reports.get(i).setTimeStamp(reports.get(i).getTimeStamp().replace(',', ' '));
                sb.append(reports.get(i).getTimeStamp());
                sb.append(',');
                sb.append(reports.get(i).getType());
                sb.append(',');
                sb.append(reports.get(i).getPatron());
                sb.append(',');
                sb.append(reports.get(i).getFirstName());
                sb.append(',');
                sb.append(reports.get(i).getLastName());
                sb.append(',');
                sb.append(reports.get(i).getWalver());
                sb.append('\n');
            }

            pw.write(sb.toString());
            pw.close();
            System.out.println("done!");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }//GEN-LAST:event_jButton2MouseClicked

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        String name = jTextField1.getText();
        String timefrom = jTextField2.getText();
        String timeto = jTextField4.getText();
        String datefrom = jTextField5.getText();
        String dateto = jTextField6.getText();
        String instructor = jTextField3.getText();
        
        if(name.length() < 4){
            JOptionPane.showMessageDialog(rootPane, "Enter name at least 4 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(!validateTime(timefrom)){
            JOptionPane.showMessageDialog(rootPane, "Enter Start Time in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(!validateTime(timeto)){
            JOptionPane.showMessageDialog(rootPane, "Enter End Time in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(!validateDate(datefrom)){
            JOptionPane.showMessageDialog(rootPane, "Enter From Date in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(!validateDate(dateto)){
            JOptionPane.showMessageDialog(rootPane, "Enter To Date in valid format.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(instructor.length() < 4){
            JOptionPane.showMessageDialog(rootPane, "Enter Instructor name at least 4 characters long.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        
        UserController userController = new UserController(this.db);
        userController.addClass(this,name,timefrom,timeto,datefrom,dateto,instructor);
        
    }//GEN-LAST:event_jButton1MouseClicked

    /**
     * @param args the command line arguments
     */
    public void main(final DBcontrol database, final String user_role) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    // Custom Coloring - Begins Here
                    UIManager.put("control", new Color(233,236,242));
                    // Custom Coloring - Ends Here
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AdminDashboard form = new AdminDashboard();
                form.db = database;
                form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                form.pack();
                form.setLocationRelativeTo(null);   //  Shows PatronDashboard in the center of the screen
                form.setVisible(true);
                form.refreshAllTables();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_deleteDrug1;
    private javax.swing.JButton btn_deleteDrug4;
    private javax.swing.JButton btn_deleteDrug7;
    private javax.swing.JButton btn_editDrug;
    private javax.swing.JButton btn_editDrug2;
    private javax.swing.JButton btn_editDrug3;
    private javax.swing.JButton btn_newDrug;
    private javax.swing.JButton btn_newDrug2;
    private javax.swing.JButton btn_newDrug3;
    private javax.swing.JButton btn_newDrug4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTbl_drugs;
    private javax.swing.JTable jTbl_drugs2;
    private javax.swing.JTable jTbl_drugs3;
    private javax.swing.JTable jTbl_drugs4;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
