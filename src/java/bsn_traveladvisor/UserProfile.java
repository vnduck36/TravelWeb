package bsn_traveladvisor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import net.bootsfaces.utils.FacesMessages;


public class UserProfile implements Serializable{

    public UserProfile(String uid) {
        this.uid = uid;
    }

    
   private String uid;
   private String name;
   private int age;
   private String gender;
   private String email;
   private String profileImg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
   
    public void updateSuccess()
    {
        FacesMessages.info("Info!", "Your <strong>profile</strong> is succesfully <strong>updated</strong>!");
        
    }
    public void updateProfile() {

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
       
        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
           
            int r = stat.executeUpdate("UPDATE bsn_userprofile SET name = '" + name + "', "
                    + "age= '" + age + "', gender= '" + gender + "',"
                            + "email = '" + email + "' where uid='"+uid+"'");
           updateSuccess();
          
        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }
}
           
    