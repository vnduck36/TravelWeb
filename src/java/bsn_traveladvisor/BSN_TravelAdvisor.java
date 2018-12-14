package bsn_traveladvisor;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BSN_TravelAdvisor {

//    public static void main(String[] args) {
//
//        System.out.println("Welcome to Travel Adviser!");
//
//        //display the main Page menu and get the input
//        Scanner input = new Scanner(System.in);
//        String selection = "";
//
//        //while loop to display menu
//        while (!selection.equalsIgnoreCase("x")) {
//            System.out.println();
//            System.out.println("Please make your selection");
//            System.out.println("1: Register an account");
//            System.out.println("2: Login to your account ");
//            System.out.println("x: EXIT");
//            System.out.print("ENTER YOUR SELECTION HERE>>");
//            //get the input
//            selection = input.nextLine();
//            System.out.println();
//
//            if (selection.equals("1")) {
//                register();
//            } else if (selection.equals("2")) {
//                login();
//            } else if (!selection.equals("x")) {
//                System.err.println("Please select correct option from the menu");
//            }
//        }
//
//    }

//    private static void register() {
//
//        //ask for user data
//        Scanner sc = new Scanner(System.in);
//        String uid;
//        String password;
//        do {
//            System.out.println("Enter Your UserID:");
//            uid = sc.next();
//
//            System.out.println("Please Enter your password");
//            password = sc.next();
//        } while (!checkValidations(uid, password));
//
//        String tags = CommonMethods.acceptTags();
//
//        String[] temp = tags.split("#");
//
//        //database operation to register
//        InsertIntoDB(uid, password, tags, temp);
//
//    }

//    public static String InsertIntoDB(String uid, String password, String tags, String[] temp) {
//        //database operation to register
//        Connection conn = null;
//        Statement stat = null;
//        ResultSet rs = null;
//
//        try {
//            conn = DatabaseUtil.getConnection();
//            stat = conn.createStatement();
//            rs = stat.executeQuery("SELECT * FROM bsn_User where uid='" + uid + "';");
//
//            if (rs.next()) {
//                System.out.println("The Id is taken. Please select another ID");
//            } else {
//                int r = stat.executeUpdate("Insert into bsn_User values('" + uid + "','" + password + "','" + tags + "','0')");
//
//                for (int i = 0; i < temp.length; i++) {
//                    int j = stat.executeUpdate("Insert into bsn_usertag values('" + uid + "','" + temp[i] + "')");
//                }
//
//                return ("The new User Account has been created successfully!");
//            }
//
//        } catch (SQLException e) {
//            
//            e.printStackTrace();
//            return ("Account creation was failed!");
//        } finally {
//            //close the DB ,statement and resultset
//            DatabaseUtil.closeConnection(conn);
//            DatabaseUtil.closeStatement(stat);
//            DatabaseUtil.closeResultSet(rs);
//        }
//    }


    private static void login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Your Login UserID : ");
        String uid = sc.next();

        System.out.println("Please Enter your Password");
        String password = sc.next();

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

            } else {
                String dbpassword = rs.getString("password");

                if (dbpassword.equals(password)) {
                    int type = rs.getInt("user_type");
                    if (type == 0) {
                       // System.out.println("user login");
                        return;
                    }
                    if (type == 1) {
                        //System.out.println("Admin login");
                        return;
                    }
                    //System.out.println("*****The LOGIN is succesful*****");
                } else {
                    System.out.println("Your password is wrong");
                }

            }

        } catch (SQLException e) {
            System.err.println("Account creation was failed!");
            e.printStackTrace();
        } finally {
            //close the DB
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }

    }

    private static boolean checkValidations(String uid, String password) {
        if (uid.length() < 3 || uid.length() > 10) {
            System.out.println("ERROR: ID Must be between 3 and 10 characters");
            return false;
        } else if (!uid.matches(".*[0-9].*") || !uid.matches(".*[a-zA-Z].*")) {
            System.out.println("ERROR: ID must contain at least one letter and one digit");
            return false;
        } else if (password.equalsIgnoreCase(uid)) {
            System.out.println("ERROR: Password can not be same as your user id.");
            return false;
        }
        return true;
    }
//
//    private static boolean isInteger(String a) {
//        try {
//            int i = Integer.parseInt(a);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
}