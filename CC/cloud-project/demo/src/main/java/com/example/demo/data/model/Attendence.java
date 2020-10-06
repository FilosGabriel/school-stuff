package com.example.demo.data.model;

import java.util.Date;

public class Attendence {
    private String personId;
    private boolean present;

    public Attendence(String personId, boolean present) {
        this.personId = personId;
        this.present = present;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }



    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
