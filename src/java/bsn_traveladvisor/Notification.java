
package bsn_traveladvisor;

/**
 *
 * @author leventleger
 */
public class Notification {
    private String answerID;
    private String answer;
    private String answerer;
    private String questionID;
    private String question;
    private String asker;
    private String nstatus;
    private String name;
    
    public Notification(String aid, String a, String aer, String qid, String q, String qer, String nst, String attr)
    {
        answerID = aid;
        answer = a;
        answerer = aer;
        questionID = qid;
        question = q;
        asker = qer;
        nstatus = nst;
        name = attr;
    }
    
    public String getAnswerID() {
        return answerID;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerer() {
        return answerer;
    }

    public void setAnswerer(String answerer) {
        this.answerer = answerer;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAsker() {
        return asker;
    }

    public void setAsker(String asker) {
        this.asker = asker;
    }

    public String getNstatus() {
        return nstatus;
    }

    public void setNstatus(String nstatus) {
        this.nstatus = nstatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
