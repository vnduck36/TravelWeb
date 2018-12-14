package bsn_traveladvisor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import net.bootsfaces.utils.FacesMessages;

//sessionScoped need to implement the interface Serializable
@ManagedBean
@SessionScoped
public class Login implements Serializable {

    //attributes
    
    private String uid;
    
    private String password;
    private int type;
    private RegularUser regularUserAccount;
    private Admin adminAccount;

    public Login() {
        regularUserAccount = null;
        adminAccount = null;
    }

    public String getUid() {
        return uid;
    }

    public String getPassword() {
        return password;
    }

    public int getType() {
        return type;
    }

    public RegularUser getRegularUserAccount() {
        return regularUserAccount;
    }

    public Admin getAdminAccount() {
        return adminAccount;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    

    public String errorPassword() {    
        FacesMessages.error("Error!", "The <strong>Password</strong> is incorrect!");
        return null;
    }
    public String errorUsername() {
        FacesMessages.error("Error!", "The <strong>Username</strong> is not in the System!");
        return null;
    }
    public String blockedUser() {
        FacesMessages.error("Error!", "The <strong>Username</strong> is already blocked!");
        return null;
    }
    public String answerNoti() {
        FacesMessages.info("Info!", "One of your questions is answered! Check the inbox!");
        return "welcomeRegularUser";
    }
    public String login() {
        
        if (uid.equals("admin") && password.equals("admin")) {
            adminAccount = new Admin(uid, password, 1);
            return "welcomeAdmin";
        }
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_User where uid = '" + uid + "'");

            if (rs.next()) {
                if (password.equals(rs.getString(2))) {
                    if (rs.getInt(4) == 9) {
                        return blockedUser();
                    }   
                    regularUserAccount = new RegularUser(uid, password, rs.getString(3), type);
                    int count = 0;
            
                    String q = "Select * from BSN_Answer natural join BSN_Question where "
                            + "Asker = '" + uid + "' and Notification = 'unread'";
                    rs = stat.executeQuery(q);
                    if(rs.next())
                    {
                        q = "Select count(AnswerID) from BSN_Answer natural join BSN_Question where "
                            + "Asker = '" + uid + "' and Notification = 'unread'";
                        rs = stat.executeQuery(q);
                        while(rs.next()){
                            count = rs.getInt(1);
                        }
                        if(count >= 1) return answerNoti();
                        else return "welcomeRegularUser";
                    }
                    else return "welcomeRegularUser";
//                    if(count == 0 || count == 1) {
//                        noti = count + " notification";
//                    }
//                    else noti = count + " notifications";
                    
                    
                } else {
                    return errorPassword();
                    
                }
            } else {
                return errorUsername();
                
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return ("internalError");
        } finally {
            
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }

    }
    
}
