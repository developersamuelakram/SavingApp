package com.example.saverapp.Model;

import com.google.firebase.firestore.DocumentId;

public class GoalsModel {
    @DocumentId
    String goalid;
    String goalname, goalamount, togo;
    int currentAmount;


    public GoalsModel() {


    }

    public GoalsModel(String goalid, String goalname, String goalamount, String togo, int currentAmount) {
        this.goalid = goalid;
        this.goalname = goalname;
        this.goalamount = goalamount;
        this.togo = togo;
        this.currentAmount = currentAmount;
    }


    public String getGoalid() {
        return goalid;
    }

    public void setGoalid(String goalid) {
        this.goalid = goalid;
    }

    public String getGoalname() {
        return goalname;
    }

    public void setGoalname(String goalname) {
        this.goalname = goalname;
    }

    public String getGoalamount() {
        return goalamount;
    }

    public void setGoalamount(String goalamount) {
        this.goalamount = goalamount;
    }

    public String getTogo() {
        return togo;
    }

    public void setTogo(String togo) {
        this.togo = togo;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }
}