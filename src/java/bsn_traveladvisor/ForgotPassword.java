package bsn_traveladvisor;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ForgotPassword {
    private String uid;
    private String password;
    private String confirmPassword;
    private int type ;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    
    public String forgotpassword() {
        
        
        
        if(!password.equals(confirmPassword)) {
            return "Password  is not matching with the Confirm Password! Please try again!";
        }
        
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();

            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_User where uid = '" + uid + "';");

            if (!rs.next()) {
                
                return "The User ID you entered is not found! Please try again!";

            } else {
                String dbpassword = rs.getString("password");
                
                int upTrue = stat.executeUpdate("Update BSN_User set Password = '" 
                        + password + "' where uid = '" + uid + "';");
                if(upTrue == 1) {
                    System.out.println("Password is changed successfully! You "
                            + "now can login with your new password!");
                    return "Password is changed successfully! You now can login with your new password!";
                } else {
                    return "Password update failed! Please try again!";
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return "internalError";
        }  
        
    }
}
