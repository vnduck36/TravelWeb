
package bsn_traveladvisor;

/**
 *
 * @author leventleger
 */
public class Question {
    private String questionID;
    private String questioner;
    private String question;
    private String attraction;
    
    public Question(String id, String asker, String q, String a){
        questionID = id;
        questioner = asker;
        question = q;
        attraction = a;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }
    
    public String getQuestioner() {
        return questioner;
    }

    public void setQuestioner(String questioner) {
        this.questioner = questioner;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAttraction() {
        return attraction;
    }

    public void setAttraction(String attraction) {
        this.attraction = attraction;
    }

 
}
