package com.emt.morph;

public class Feedback {
    private String emailId;
    private String comment;
    private boolean valid;

    public Feedback() {
    }

    public Feedback(String emailId, String comment) {
        this.emailId = emailId;
        this.comment = comment;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
