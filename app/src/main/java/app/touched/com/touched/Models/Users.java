package app.touched.com.touched.Models;

/**
 * Created by Anshul on 2/24/2018.
 */

public class Users {
    private String user_id;
    private String push_token;
    private String emailId;
    private String last_login_time;

    private String last_online_time;
     private String gifts_counts;

    public String getGifts_counts() {
        return gifts_counts;
    }

    public void setGifts_counts(String gifts_counts) {
        this.gifts_counts = gifts_counts;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    private String refund_amount;

    public Users() {
    }

    public Users(String user_id, String emailId, String last_login_time, String is_login, String last_online_time) {
        this.user_id = user_id;
        this.emailId = emailId;
        this.last_login_time = last_login_time;

        this.last_online_time = last_online_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPush_token() {
        return push_token;
    }

    public void setPush_token(String push_token) {
        this.push_token = push_token;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }



    public String getLast_online_time() {
        return last_online_time;
    }

    public void setLast_online_time(String last_online_time) {
        this.last_online_time = last_online_time;
    }

   }
