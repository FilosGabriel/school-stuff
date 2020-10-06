package com.example.morsecode.SMS;

public class SMS {

    private String person;
    private String message;

    public String getPerson() {
        return person;
    }

    public String getMessageInternaly() {
        return message.replaceAll("       ", " ").replaceAll("   ", "_");
    }

    public String getMessage() {
        return message;
    }


    public SMS(String person, String message) {
        this.person = person;
        this.message = message;
    }
}
