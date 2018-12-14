
package bsn_traveladvisor;

/**
 *
 * @author leventleger
 */
public class Answer {
    private String answerID;
    private String questionID;
    private String question;
    private String answerer;
    private String answer;
    private String attraction;
    
    public Answer(String aid, String qid, String q, String ansby, String a, String attr){
        answerID = aid;
        questionID = qid;
        question = q;
        answerer = ansby;
        answer = a;
        attraction = attr;
    }

    public String getAnswerID() {
        return answerID;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getAnswerer() {
        return answerer;
    }

    public void setAnswerer(String answerer) {
        this.answerer = answerer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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
