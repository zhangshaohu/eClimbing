/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package psis;

import psis.Controller.DBcontrol;
import psis.View.PatronDashboard;
import psis.View.LoginView;
import psis.View.RegisterView;


public class PSIS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DBcontrol db = new DBcontrol();
        db.createDBcon();
        
        LoginView login = new LoginView();
        login.main(db);
        
//        RegisterView login = new RegisterView();
//        login.main(db);
        
//        PatronDashboard form = new PatronDashboard();
//        form.main(db,"admin");

//        db.terminateDBcon();
    }
}
