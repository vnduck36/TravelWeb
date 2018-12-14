/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsn_traveladvisor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;


@ManagedBean
@ApplicationScoped
public class User {
    
    private String uid;
    private String password;
    private int type;

    
    public User(String uid, String password, int type) {
        this.uid = uid;
        this.password = password;
        this.type = type;
    }
    

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
    
    public String login() {

        //database operation
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();

            stat = conn.createStatement();
            rs = stat.executeQuery("SELECT * FROM User where uid='" + uid + "';");

            if (!rs.next()) {
                System.out.println("The Id is Not Found");
                return "loginFailed";

            } else {
                String dbpassword = rs.getString("password");

                if (dbpassword.equals(password)) {
                    int type = rs.getInt("user_type");
                    if (type == 1) {
                        //System.out.println("Admin login");
                        //User user =new Admin(uid,password,type);
                        //.welcome();
                        return "adminHome";
                    } else {
                       // System.out.println("user login");
                        //User user=new RegularUser(uid,password,type);
                        //user.welcome();
                        return "userhome";
                    }
                    //System.out.println("*****The LOGIN is succesful*****");
                } else {
                    System.out.println("Your password is wrong");
                    return "loginFailed";
                }

            }

        } catch (SQLException e) {
            System.err.println("Account creation was failed!");
            e.printStackTrace();
            return "error";
        } finally {
            //close the DB
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
        
    }
}
