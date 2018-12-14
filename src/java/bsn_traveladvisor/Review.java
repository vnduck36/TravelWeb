
package bsn_traveladvisor;

/**
 *
 * @author leventleger
 */
public class Review {
    private String uid;
    private String attraction;
    private String review;
    
    public Review(String id, String a, String r){
        uid = id;
        attraction = a;
        review = r;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAttraction() {
        return attraction;
    }

    public void setAttraction(String attraction) {
        this.attraction = attraction;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
    
    
}
