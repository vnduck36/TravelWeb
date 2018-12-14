package bsn_traveladvisor;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.pathType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.validation.constraints.Pattern;
import net.bootsfaces.utils.FacesMessages;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@ManagedBean
@RequestScoped
public class Registration {

    @NotEmpty(message="The username cannot be empty")
    @Pattern(message="The username must has at least 1 letter and 1 digit",regexp = "(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+).{3,10}")
    private String uid;
    
    @Length(message="The length of the password is between 3 and 20",min=3, max=20)
    @NotEmpty(message="The password cannot be empty")
    private String password;
    
    private String confirmPassword;//not required
    //private String[] ptags;
    private int type;
    
    private String ptag = "History Buff";
    private List<SelectMultiMenu> ptags = new ArrayList<>();
    
    public Registration() {
            getPtags().add(new SelectMultiMenu("History Buff", "History Buff"));
            getPtags().add(new SelectMultiMenu("Shopping Fanatic", "Shopping Fanatic"));
            getPtags().add(new SelectMultiMenu("Beach Goer", "Beach Goer"));
            getPtags().add(new SelectMultiMenu("Urban Explorer", "Urban Explorer"));
            getPtags().add(new SelectMultiMenu("Nature Lover", "Nature Lover"));
            getPtags().add(new SelectMultiMenu("Family Vacationer", "Family Vacationer"));
    }
    
    public void updateTagMessage() {
        if (null == ptag) {
                FacesMessages.warning("**:ptag", "Warning", "No Tag Selected!");
        } else {
                FacesMessages.info("**:ptag", "Tag Selected", "Selected Tag " + ptag);
        }
    }
    
   
    
    public void errorMatching() {
        FacesMessages.error("Error!", "The <strong>Username</strong> must be different from the <strong>Password</strong>.");
    }
    
    public void usernameError() {
        FacesMessages.error("Error!", "The <strong>Username</strong> is taken. Please choose a new one!");
    }
    
    public void accountSuccess() {
        FacesMessages.info("Info!", "Your new <strong>User Accounte</strong> is succesfully signed up!");
    }
    public void register() {
        //  System.out.println(ptags);
        String[] ptA; String t = "";

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();

            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_User where uid = '" + uid + "'");

            if (rs.next()) {
                usernameError();
            } else {
                if(uid.equals(password)){
                    errorMatching();
                }
                else
                {
                    ptA = ptag.split(",");
                    for (int i = 0; i < ptA.length; i++) {
                        t = t + ptA[i] + ", ";
                        int j = stat.executeUpdate("Insert into BSN_UserTag values('" + uid + "','" + ptA[i] + "')");
                    }

                    int r = stat.executeUpdate("Insert into BSN_User values('" + uid + "','" + password + "','" + t + "','0')");
                    int r1 = stat.executeUpdate("Insert into BSN_Userprofile values('" + uid + "','','0','','','')");
                    accountSuccess();
                }
                
                //return ("The new User Account has been created successfully!");
            }

        } catch (SQLException e) {
            
            e.printStackTrace();
            //return ("Internal Error! Please Try Again later!");
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }

    }
    
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

    public List<SelectMultiMenu> getPtags() {
        return ptags;
    }

    public void setPtags(List<SelectMultiMenu> ptags) {
        this.ptags = ptags;
    }

    public String getPtag() {
        return ptag;
    }

    public void setPtag(String ptag) {
        this.ptag = ptag;
    }
    
    public void checkUsername() {
	FacesMessages.error("Error!", "Something has gone <strong>wrong</strong>.");
    }

    

}
