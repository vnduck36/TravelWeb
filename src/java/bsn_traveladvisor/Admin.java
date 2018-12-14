package bsn_traveladvisor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import net.bootsfaces.utils.FacesMessages;

@ManagedBean
@SessionScoped
public class Admin implements Serializable {

    private String uid;
    private String password;
    private int type;
    //public List<Attraction> pendingRequests;

    public Admin(String uid, String password, int type) {
        this.uid = uid;
        this.password = password;
        this.type = type;

    }

    private String name;
    private String description;
    private String city;
    private String state;
    private String atags;
    private String status;
    private String requestID;
    private String requester;

    public String sa;

    public String getSa() {
        return sa;
    }

    public void setSa(String sa) {
        this.sa = sa;
    }

    public List<Attraction> getAttractionRequests() {

        List<Attraction> pendingRequests;
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_AttrReq where status = 'pending'");

            pendingRequests = new ArrayList<Attraction>();
            while (rs.next()) {
                Attraction a = new Attraction(requestID, requester, name, description, city, state, atags, status, null);
                a.setRequestID(rs.getString(1));
                a.setRequester(rs.getString(2));
                a.setName(rs.getString(3));
                a.setDescription(rs.getString(4));
                a.setCity(rs.getString(5));
                a.setState(rs.getString(6));
                a.setAtags(rs.getString(7));
                a.setStatus(rs.getString(8));

                pendingRequests.add(a);

            }

            return pendingRequests;

        } catch (SQLException e) {
            System.err.println("Account creation was failed!");
            e.printStackTrace();
            return null;
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }

    }

    public void approveSuccess()
    {
        FacesMessages.info("Info!", "A <strong>new attraction request</strong> is succesfully <strong>approved</strong>!");
        
    }
    public void approveAttraction(String a) {
        System.out.println(a);
        System.out.println(sa);

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_AttrReq where AttractionName ='" + sa + "'");

            if (rs.next()) {
                requestID = rs.getString(1);
                requester = rs.getString(2);
                name = rs.getString(3);
                description = rs.getString(4);
                city = rs.getString(5);
                state = rs.getString(6);
                atags = rs.getString(7);

              
                int r = stat.executeUpdate("Update BSN_AttrReq set Status "
                        + "= 'approved' where AttractionName ='" + sa + "'");

                String[] tags = atags.split("\\s*,\\s");
                ArrayList<String> tagNames = new ArrayList<String>();
                for(int i = 0; i < tags.length; i++)
                {
                    tagNames.add(tags[i]);
                }
                StringBuilder tagList = new StringBuilder();
                            
                for (int i = 0; i < tagNames.size(); i++)
                {
                    tagList.append(tagNames.get(i).trim());
                    if (i >= 0 && i < tagNames.size() - 1) {
                        tagList.append(", ");
                    }
                }
                
                String ats = tagList.toString();
                String[] atagsArr = ats.split(",");
                
                int t = stat.executeUpdate("Insert into BSN_Attraction values "
                        + "('" + name + "', '" + description + "', '" + city
                        + "', '" + state + "', '" + ats + "', -1, '" + requester + "')");

                for(int i = 0; i < atagsArr.length; i++)
                {
                    int j = stat.executeUpdate("Insert into BSN_AttrTag values('"
                            + name + "','" + atagsArr[i] + "')");
                }
                
                
            }
            approveSuccess();

        } catch (SQLException e) {
            
            e.printStackTrace();
            //return ("internalError");
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    public void rejectSuccess()
    {
        FacesMessages.error("Error!", "A <strong>new attraction request</strong> is succesfully <strong>rejected</strong>!");
        
    }
    public void rejectAttraction() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            System.out.println(sa);
            rs = stat.executeQuery("Select * from BSN_AttrReq where AttractionName = '"
                    + sa + "' and status = 'pending'");

            if (rs.next()) {
                int ra = stat.executeUpdate("Update BSN_AttrReq set Status "
                        + "= 'rejected' where AttractionName = '" + sa + "'");
            }

            //  return ("The attraction " + name + " is succesfully rejected!");
//            return ("attractionRejection");
                rejectSuccess();
        } catch (SQLException e) {
            
            e.printStackTrace();
            //return ("internalError");
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    //block User   
    public String blockUser(String a,int ty) {
        System.out.println(a);

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            int r = stat.executeUpdate("Update BSN_User set user_type="+ty+" where uid ='" + a + "'");

            if (ty==0) {
                return ("unblockuserapproval");
            }
            return ("blockuserapproval");

        } catch (SQLException e) {
            System.err.println("block User was failed!");
            e.printStackTrace();
            return ("internalError");
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }
    
        //block User   
    public String unBlockUser(String a) {
        System.out.println(a);

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            int r = stat.executeUpdate("Update BSN_User set user_type=0 where uid ='" + a + "'");

            return ("unblockuserapproval");

        } catch (SQLException e) {
            System.err.println("Un block User was failed!");
            e.printStackTrace();
            return ("internalError");
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    public List<RegularUser> getUsers(int ty) {

        List<RegularUser> allUsers;
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_User where user_type = "+ty);

            allUsers = new ArrayList<RegularUser>();
            while (rs.next()) {
                RegularUser a = new RegularUser();
                a.setUid(rs.getString(1));
                allUsers.add(a);
            }

            return allUsers;

        } catch (SQLException e) {
            System.err.println("Get Users was failed!");
            e.printStackTrace();
            return null;
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }

    }

    public String signOut() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml";
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAtags() {
        return atags;
    }

    public void setAtags(String atags) {
        this.atags = atags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

}
