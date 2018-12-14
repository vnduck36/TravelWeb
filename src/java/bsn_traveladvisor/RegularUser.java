package bsn_traveladvisor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import net.bootsfaces.utils.FacesMessages;

@ManagedBean
@SessionScoped
public class RegularUser implements Serializable {

    private String uid;
    private String password;
    private int type;
    private String pTags; //profile tags


    public RegularUser(String uid, String password, String pT, int type) {
        this.uid = uid;
        this.password = password;
        this.pTags = pT;
        this.type = type;
        cu = new UserProfile(uid);
    }

    //create attraction attributes
    public String name = "";
    private String description = "";
    private String city = "";
    private String state = "";
    private String[] aTags; //array of attraction tags
    private String status = "";
    private String requestID = "";
    private String requester = "";
    
    private double score = -1;

    public Attraction ad = new Attraction(null, null, "NASA", null, null, null, null, null, 0.0);

    private String atag; //each attraction tag

    public String selectedAttr; //selected attraction
    public String selectedQuest; //selected question
    public String selectedAns; //selected answer
    public String review;
    public String question;
    public String answer;
    public String asker;
    public String answerer;
    public String questionID;
    public String answerID;
    public String questionsel;

    public String message;

    String t = "";//attraction tags string
    String aview = ""; //attraction details string

    List<Attraction> attraction;
    DecimalFormat df = new DecimalFormat("###.##"); //round the average score 2 decimal

    ArrayList<String> favCity;
    ArrayList<String> favAttr;

    List<Review> rev; //list of reviews
    List<Question> quest; //list of questions
    List<Answer> ans; //list of answers

    public UserProfile cu;

    
    //attraction request part
    public RegularUser() {
        getAtags().add(new SelectMultiMenu("History", "History"));
        getAtags().add(new SelectMultiMenu("Shopping", "Shopping"));
        getAtags().add(new SelectMultiMenu("Beach", "Beach"));
        getAtags().add(new SelectMultiMenu("Urban", "Urban"));
        getAtags().add(new SelectMultiMenu("Nature", "Nature"));
        getAtags().add(new SelectMultiMenu("Family", "Family"));
    }
    private String at = "History";
    private List<SelectMultiMenu> atags = new ArrayList<>();
   
    public void updateProfileTagMessage() {
        if (null == at) {
                FacesMessages.warning("**:ptag", "Warning", "No Tag Selected!");
        } else {
                FacesMessages.info("**:ptag", "Tag Selected", "Selected Tag " + at);
        }
    }
    public void attractionRequestSuccess() {
        FacesMessages.info("Info!", "A <strong>new attraction request</strong> is succesfully sent to the <strong>Admin</strong>!");
    }
    public void createAttractionRequest() {

        String requestID = "";
        String status = "pending";
        String t = ""; String[] atA;
//        for (int i = 0; i < aTags.length; i++) {
//            t = t + aTags[i] + ", ";
//        }

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_NextAttrReqID");

            if (rs.next()) {
                requestID = "R" + rs.getInt(1);
            }

            int r = stat.executeUpdate("Update BSN_NextAttrReqID set "
                    + "nextID = '" + (rs.getInt(1) + 1) + "'");
            
            atA = at.split(",");
            for (int i = 0; i < atA.length; i++) {
                t = t + atA[i] + ", ";
            }
            
            int j = stat.executeUpdate("Insert into BSN_AttrReq values ('" + requestID
                    + "', '" + uid + "', '" + name + "', '" + description
                    + "', '" + city + "', '" + state + "', '" + t
                    + "', 'pending')");

            attractionRequestSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
            //return ("internalError Occured!");
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }

    }

    public String viewAttraction() {
        System.out.println("------------------------------------");
        System.out.println(selectedAttr);

        for (Attraction a : attraction) {
            if (a.getName().equals(selectedAttr)) {
                name = a.getName();
                description = a.getDescription();
                city = a.getCity();
                state = a.getState();
                at = a.getAtags();
                score = a.getScore();
                aview = a.getName() + " is a " + a.getDescription()
                        + ", located in " + a.getCity() + " " + a.getState()
                        + " which has tags " + a.getAtags();
            }
        }
        
        //update attraction view table count
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        int countView = 0, newCountView = 0;
        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            //check if the attraction and uid already in the AttrView DB
            String q = "Select * From BSN_AttrView where uid = '"
                    + uid + "' and AttractionName = '" + selectedAttr + "'";
            rs = stat.executeQuery(q);

            if (rs.next()) {
                int t = stat.executeUpdate("Update BSN_AttrView set ViewCount = "
                        + (rs.getInt(3) + 1) + " where uid = '" + uid
                        + "' and AttractionName = '" + selectedAttr + "'");

                q = "Select sum(ViewCount) from BSN_AttrView where AttractionName = '" + selectedAttr + "'";
                rs = stat.executeQuery(q);
                while (rs.next()) {
                    newCountView = rs.getInt(1);
                }

                //  return (newCountView + " views");
            } else {
                int r = stat.executeUpdate("Insert into BSN_AttrView values "
                        + "('" + uid + "', '" + selectedAttr + "', " + (countView + 1) + ")");

                q = "Select sum(ViewCount) from BSN_AttrView where AttractionName = '" + selectedAttr + "'";
                rs = stat.executeQuery(q);
                while (rs.next()) {
                    newCountView = rs.getInt(1);
                }

                //return (newCountView + " view");
            }

        } catch (SQLException e) {
            System.err.println("view attarction was failed!");
            e.printStackTrace();
            return null;
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
        if(newCountView == 0 || newCountView == 1)
        {
            return aview + " - " + newCountView + " view";
        }
        else
        {
            return aview + " - " + newCountView + " views";
        }

    }
    public List<Attraction> getAttractionByCity() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            rs = stat.executeQuery("Select * from BSN_Attraction where City = '" + city + "' order by score desc");

            attraction = new ArrayList<Attraction>();

            while (rs.next()) {
                Attraction a = new Attraction(null, requester, name, description, city, state, t, null, score);

                a.setName(rs.getString(1));
                a.setDescription(rs.getString(2));
                a.setCity(rs.getString(3));
                a.setState(rs.getString(4));
                a.setAtags(rs.getString(5));
                a.setScore(rs.getDouble(6));
                a.setRequester(rs.getString(7));
                attraction.add(a);

            }

            return attraction;

        } catch (SQLException e) {
            System.err.println("Attraction creation was failed!");
            e.printStackTrace();
            return null;
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }

    }
    public List<Attraction> getAttractionByTag() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();

            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_Attraction where AttractionTags like '%" + atag + "%'  order by score desc");

            attraction = new ArrayList<Attraction>();
            while (rs.next()) {

                Attraction a = new Attraction(null, requester, name, description, city, state, t, null, score);

                a.setName(rs.getString(1));
                a.setDescription(rs.getString(2));
                a.setCity(rs.getString(3));
                a.setState(rs.getString(4));
                a.setAtags(rs.getString(5));
                a.setScore(rs.getDouble(6));
                a.setRequester(rs.getString(7));
                attraction.add(a);

            }

            return attraction;

        } catch (SQLException e) {
            System.err.println("Search attraction was failed!");
            e.printStackTrace();
            return null;
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    
    //add city to favorites
    public void addCityFavoriteSuccess() {
        FacesMessages.info("Info!", "The <strong>city</strong> is succesfully added to your <strong>My Favorites</strong>!");
    }
    public void addCityFavoriteFailure() {
        FacesMessages.error("Error!", "The <strong>city</strong> is added to your <strong>My Favorites</strong> before!");
    }
    public void addCityToFavorite() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            //check if the city is already in the User Favorite DB
            String f = "Select * From BSN_UserFavCity where uid ='"
                    + uid + "' and City = '" + city + "'";
            rs = stat.executeQuery(f);
            if (rs.next()) {
                addCityFavoriteFailure();
            } else {
                int r = stat.executeUpdate("Insert into BSN_UserFavCity values "
                        + "('" + uid + "', '" + city + "')");

                addCityFavoriteSuccess();
            }

        } catch (SQLException e) {
            
            e.printStackTrace();
            
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    public String favoriteCity() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_UserFavCity where uid = '" + uid + "'");

            favCity = new ArrayList<String>();
            String str = "";
            if (!rs.next()) {
                return ("You currently have no favorite city!");
            } else {
                rs.beforeFirst();
                while (rs.next()) {
                    city = rs.getString(2);
                    favCity.add(city);
                }

                for (String c : favCity) {
                    str += c + ",";
                }
                String str1 = str.substring(0, str.length() - 1);
                return str1;
            }

        } catch (SQLException e) {
            System.err.println("Account creation was failed!");
            e.printStackTrace();
            return ("Internal Error! Please Try Again later!");
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    public List<Attraction> favoriteAttraction() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            //   rs = stat.executeQuery("Select * from BSN_UserFav where uid = '" + uid + "'");
            rs = stat.executeQuery("select * from BSN_attraction where AttractionName in (select AttractionName from BSN_userfav where uid='" + uid + "')  order by score desc");
//            favAttr = new ArrayList<String>();
//            String s = "";
//            if (!rs.next()) {
//                //return ("No favorite attraction is recorded yet!");
//                return null;
//            } else {
//                rs.beforeFirst();
//                while (rs.next()) {
//                    name = rs.getString(2);
//                    favAttr.add(name);
//                }
//
//                for (String a : favAttr) {
//                    s += a + ", ";
//                }
//                return favAttr;
//            }
            attraction = new ArrayList<Attraction>();
            while (rs.next()) {

                Attraction a = new Attraction(null, requester, name, description, city, state, t, null, score);

                a.setName(rs.getString(1));
                a.setDescription(rs.getString(2));
                a.setCity(rs.getString(3));
                a.setState(rs.getString(4));
                a.setAtags(rs.getString(5));
                a.setScore(rs.getDouble(6));
                a.setRequester(rs.getString(7));
                attraction.add(a);

            }

            return attraction;

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

    public List<Attraction> youMayLikeAttractions() {

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            attraction = new ArrayList<Attraction>();

            rs = stat.executeQuery("select * from bsn_attraction where AttractionName in (Select AttractionID from bsn_attrtag where tag IN (select tag from bsn_usertag where uid='" + uid + "')) order by score desc limit 3");
            rs = stat.executeQuery("select * from bsn_attraction order by score desc limit 3");

            while (rs.next()) {

                Attraction a = new Attraction(null, requester, name, description, city, state, t, null, score);

                a.setName(rs.getString(1));
                a.setDescription(rs.getString(2));
                a.setCity(rs.getString(3));
                a.setState(rs.getString(4));
                a.setAtags(rs.getString(5));
                a.setScore(rs.getDouble(6));
                a.setRequester(rs.getString(7));
                attraction.add(a);

            }
            if (attraction.size() == 0) {
                attraction.add(ad);
            }
            return attraction;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }

    }

    //add attraction to favorites
    public void addAttractionFavoriteSuccess() {
        FacesMessages.info("Info!", "The <strong>attraction</strong> is succesfully added to your <strong>My Favorites</strong>!");
    }
    public void addAttractionFavoriteFailure() {
        FacesMessages.error("Error!", "The <strong>attraction</strong> is added to your <strong>My Favorites</strong> before!");
    }
    public void addAttractionToFavorite() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            //check if the attration is already in the User Favorite DB
            String f = "Select * From BSN_UserFav where uid ='"
                    + uid + "' and AttractionName = '" + selectedAttr + "'";
            rs = stat.executeQuery(f);
            if (rs.next()) {
                addAttractionFavoriteFailure();
            } else {
                int r = stat.executeUpdate("Insert into BSN_UserFav values "
                        + "('" + uid + "', '" + selectedAttr + "')");
                addAttractionFavoriteSuccess();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    //rate score
    public void rateScoreSuccess() {
        FacesMessages.info("Info!", "Your <strong>attraction score</strong> is succesfully submitted!");
    }
    public void rateScoreFailure() {
        FacesMessages.error("Error!", "You scored this <strong>attraction</strong> before!");
    }
    public void rateScore() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            rs = stat.executeQuery("Select * from BSN_Score where uid = '" + uid + "' "
                    + "and AttractionName='" + selectedAttr + "'");

            if (rs.next()) {
                rateScoreFailure();
            } else {
                int t = stat.executeUpdate("Insert into BSN_Score values('"
                        + uid + "','" + selectedAttr + "', " + score + ")");
                rateScoreSuccess();
            }

            String q = "Select avg(Score) as avg_score from BSN_Score where Score "
                    + "not in (-1) and AttractionName = '" + selectedAttr + "'";
            rs = stat.executeQuery(q);
            rs.next();
            String avg_score_str = rs.getString("avg_score");
            float avg_score = Float.parseFloat(avg_score_str);

            int y = stat.executeUpdate("Update BSN_Attraction set Score = "
                    + avg_score + " where AttractionName = '" + selectedAttr + "'");

        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    //review
    public void reviewSuccess() {
        FacesMessages.info("Info!", "Your <strong>review</strong> is succesfully submitted!");
    }
    public void reviewFailure() {
        FacesMessages.error("Error!", "You reviewed this <strong>attraction</strong> before!");
    }
    public void review() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_Review where uid = '" + uid + "' and AttractionName = '" + name + "'");
            if(rs.next()){
                reviewFailure();
            }
            else
            {
                //insert to Review table
                int t = stat.executeUpdate("Insert into BSN_Review values('"
                        + uid + "','" + name + "', '" + review + "')");
                reviewSuccess();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    public List<Review> readReview() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_Review where "
                    + "AttractionName = '" + name + "'");

            rev = new ArrayList<Review>();
//            if (!rs.next()) {
//                Review r = new Review(uid, name, review);
//                r.setUid("None");
//                r.setReview("None");
//
//                rev.add(r);
//            }
//            rs.beforeFirst();
            while (rs.next()) {

                Review r = new Review(uid, name, review);
                r.setUid(rs.getString(1));
                r.setReview(rs.getString(3));

                rev.add(r);
            }

            return rev;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }

    }

    //ask question
    public void askSuccess() {
        FacesMessages.info("Info!", "You just succesfully submitted a <strong>question</strong>!");
    }
    public void askQuestion() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            String q = "Select * from BSN_NextQuestionNumber";

            rs = stat.executeQuery(q);
            if (rs.next()) {
                questionID = "Q" + rs.getInt(1);
            }
            int r = stat.executeUpdate("Update BSN_NextQuestionNumber set "
                    + "NextId = " + (rs.getInt(1) + 1) + "");

            //insert into question table
            int t = stat.executeUpdate("Insert into BSN_Question values('"
                    + questionID + "', '" + uid + "', '" + question
                    + "', '" + name + "')");

            askSuccess();

        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    public List<Question> displayQuestion() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_Question where "
                    + "AttractionName = '" + name + "'");

            quest = new ArrayList<Question>();

            while (rs.next()) {
                Question q = new Question(questionID, asker, question, name);

                q.setQuestionID(rs.getString(1));
                q.setQuestioner(rs.getString(2));
                q.setQuestion(rs.getString(3));
                q.setAttraction(rs.getString(4));

                quest.add(q);
            }
            return quest;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }
    public String mansques = "";

    //answer question
    public void answerSuccess() {
        FacesMessages.info("Info!", "You succesfully submitted an <strong>answer</strong> for the question!");
    }
    public void answerFailure() {
        FacesMessages.error("Error!", "You answered this<strong>question</strong> before!");
    }
    public void answerQuestion() {
        Connection conn = null;
        Statement stat = null;
        Statement stat1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            stat1 = conn.createStatement();

            String q = "Select * from BSN_NextAnswerNumber";

            rs = stat.executeQuery(q);
            if (rs.next()) {
                answerID = "A" + rs.getInt(1);
            }

            String q1 = "Select * from BSN_Answer where QuestionID = '" + selectedQuest + "' and AnsweredUser='" + uid + "'";
            rs1 = stat1.executeQuery(q1);
            if (rs1.next()) {
                int r = stat.executeUpdate("Update BSN_Answer set Answer = '" + answer + "' where QuestionID = '" + selectedQuest + "' and AnsweredUser='" + uid + "'");
                answerFailure();
            } else {
                int r = stat.executeUpdate("Update BSN_NextAnswerNumber set NextId =" + (rs.getInt(1) + 1));

                //insert into answer table
                int t = stat.executeUpdate("Insert into BSN_Answer values('"
                        + answerID + "', '" + selectedQuest + "', '" + uid
                        + "', '" + answer + "', 'true')");
                answerSuccess();
            }

        } 
        catch (SQLException e) {
            e.printStackTrace();
            //return "Error in answerQuestion";
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeResultSet(rs1);
        }
    }

    public List<Answer> displayAnswer() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_Answer natural join BSN_Question "
                    + "where QuestionID = '" + selectedQuest + "'");

            ans = new ArrayList<Answer>();

            while (rs.next()) {
                Answer r = new Answer(answerID, questionID, question, answerer, answer, name);

                r.setAnswerID(rs.getString(2));
                r.setQuestionID(rs.getString(1));
                r.setAnswerer(rs.getString(3));
                r.setAnswer(rs.getString(4));

                ans.add(r);
            }
            return ans;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    //like an attraction
    public void likeSuccess() {
        FacesMessages.info("Info!", "You just succesfully liked the <strong>attraction</strong>!");
    }
    public void like() {
       
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            //check if the attraction and uid already in the LikeDislike DB
            String q = "Select * From BSN_LikeDislike where uid = '"
                    + uid + "' and AttractionName = '" + selectedAttr + "'";
            rs = stat.executeQuery(q);

            int like, dislike;
            if (rs.next()) {

                int t = stat.executeUpdate("Update BSN_LikeDislike set "
                        + "DislikeCount = 0, LikeCount = 1 where uid = '"
                        + uid + "' and AttractionName = '" + selectedAttr + "'");
                likeSuccess();

            } else {

                int r = stat.executeUpdate("Insert into BSN_LikeDislike values "
                        + "('" + uid + "', '" + selectedAttr + "', 1, 0)");
                likeSuccess();
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    //dislike an attraction
    public void dislikeSuccess() {
        FacesMessages.info("Info!", "You just succesfully disliked the <strong>attraction</strong>!");
    }
    public void disLike() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            //check if the attraction and uid already in the LikeDislike DB
            String q = "Select * From BSN_LikeDislike where uid = '"
                    + uid + "' and AttractionName = '" + selectedAttr + "'";
            rs = stat.executeQuery(q);

            if (rs.next()) {

                int t = stat.executeUpdate("Update BSN_LikeDislike set "
                        + "DislikeCount = 1, LikeCount = 0 where uid = '"
                        + uid + "' and AttractionName = '" + selectedAttr + "'");
                dislikeSuccess();
            } else {

                int r = stat.executeUpdate("Insert into BSN_LikeDislike values "
                        + "('" + uid + "', '" + selectedAttr + "', 0, 1)");
                dislikeSuccess();
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    public String countLike() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            int countLike = 0;
            String q = "Select sum(LikeCount) from BSN_LikeDislike where AttractionName = '" + selectedAttr + "'";
            rs = stat.executeQuery(q);
            while (rs.next()) {
                countLike = rs.getInt(1);
            }
            if(countLike == 0 || countLike == 1)
            {
                return (countLike + " like");
            }
            else
            {
                return (countLike + " likes");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return ("internalError");
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    public String countDislike() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();

            int countDisLike = 0;
            String q = "Select sum(DislikeCount) from BSN_LikeDislike where AttractionName = '" + selectedAttr + "'";
            rs = stat.executeQuery(q);
            while (rs.next()) {
                countDisLike = rs.getInt(1);
            }
            if(countDisLike == 0 || countDisLike == 1)
            {
                return (countDisLike + " dislike");
            }
            else
            {
                return (countDisLike + " dislikes");
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
            return ("internalError");
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }

    //get notification when someone answers questions
    String noti = "";
    public void answerNoti() {
        FacesMessages.info("Info!", "One of your questions is answered! Check the inbox!");
    }
    public void getNotification() 
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
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
                
            }
            
            if(count == 0 || count == 1) {
                noti = count + " notification";
            }
            else noti = count + " notifications";
            answerNoti();
            //return noti;
            

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
    
    List<Answer> ansNoti; //list of answer notifications
    public List<Answer> displayNotification() 
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            
            String q = "Select * from BSN_Answer natural join BSN_Question where Asker = '" + uid 
                    + "' and Notification = 'unread'";
            
            rs = stat.executeQuery(q);
            
            ansNoti = new ArrayList<Answer>();

            while(rs.next())
            {
                Answer n = new Answer(answerID, questionID, question, answerer, answer, name);
                
                n.setAnswerID(rs.getString(2));
                n.setQuestionID(rs.getString(1));
                n.setAnswerer(rs.getString(3));
                n.setQuestion(rs.getString(7));
                n.setAttraction(rs.getString(8));
                ansNoti.add(n);
            }
            
            return ansNoti;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //close the DB ,statement and resultset
            DatabaseUtil.closeConnection(conn);
            DatabaseUtil.closeStatement(stat);
            DatabaseUtil.closeResultSet(rs);
        }
    }
    
    
    public String readAnswer()
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from BSN_Answer natural join BSN_Question "
                    + "where AnswerID = '" + selectedAns + "'");
            while(rs.next())
            {
                answerer = rs.getString(3);
                answer = rs.getString(4);
                question = rs.getString(7);
                name = rs.getString(8);
            }
            int r = stat.executeUpdate("Update BSN_Answer set Notification "
                    + "= 'read' where AnswerID = '" + selectedAns + "'");
            
            return answer;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "internal Error";
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

    public String getpTags() {
        return pTags;
    }

    public void setpTags(String pTags) {
        this.pTags = pTags;
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

    public String[] getaTags() {
        return aTags;
    }

    public void setaTags(String[] aTags) {
        this.aTags = aTags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getSelectedAttr() {
        return selectedAttr;
    }

    public void setSelectedAttr(String selectedAttr) {
        this.selectedAttr = selectedAttr;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getAtag() {
        return atag;
    }

    public void setAtag(String atag) {
        this.atag = atag;
    }

    public List<Attraction> getAttraction() {
        return attraction;
    }

    public List<String> getFavCity() {
        return favCity;
    }

    public List<String> getFavAttr() {
        return favAttr;
    }

    public List<Review> getRev() {
        return rev;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getAsker() {
        return asker;
    }

    public void setAsker(String asker) {
        this.asker = asker;
    }

    public List<Question> getQuest() {
        return quest;
    }

    public String getSelectedQuest() {
        return selectedQuest;
    }

    public void setSelectedQuest(String selectedQuest) {
        this.selectedQuest = selectedQuest;
    }

    public String getAnswerID() {
        return answerID;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public String getAnswerer() {
        return answerer;
    }

    public void setAnswerer(String answerer) {
        this.answerer = answerer;
    }

    public String getQuestionsel() {
        return questionsel;
    }

    public void setQuestionsel(String questionsel) {
        this.questionsel = questionsel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserProfile getCu() {
        return cu;
    }

    public void setCu(UserProfile cu) {
        this.cu = cu;
    }

    public List<Answer> getAnsNoti() {
        return ansNoti;
    }

    public String getSelectedAns() {
        return selectedAns;
    }

    public void setSelectedAns(String selectedAns) {
        this.selectedAns = selectedAns;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public List<SelectMultiMenu> getAtags() {
        return atags;
    }

    
    public String getMansques() {
        return mansques;
    }

    public void setMansques(String mansques) {
        this.mansques = mansques;
    }

    
}
