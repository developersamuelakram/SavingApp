package com.example.saverapp.Model;

import com.google.firebase.firestore.DocumentId;

public class TransModel {
    @DocumentId
    String id;

    String trans;


    public TransModel() {
    }

    public TransModel(String id, String trans) {
        this.id = id;
        this.trans = trans;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }
}
