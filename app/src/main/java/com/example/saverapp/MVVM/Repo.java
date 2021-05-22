package com.example.saverapp.MVVM;

import androidx.annotation.Nullable;

import com.example.saverapp.Model.GoalsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Repo {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String userid;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    List<GoalsModel> goalsModelList = new ArrayList<>();
    OnGoalsAdded onGoalAdded;

    public Repo(OnGoalsAdded onGoalAdded) {
        this.onGoalAdded = onGoalAdded;
    }

    public void getAllGoals(){


        userid = firebaseAuth.getCurrentUser().getUid();


        firebaseFirestore.collection("Goal"+userid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot value, FirebaseFirestoreException error) {


                goalsModelList.clear();

                if (value!=null) {

                    for (DocumentSnapshot ds: value.getDocuments()) {

                        GoalsModel goalsModel = ds.toObject(GoalsModel.class);

                        goalsModelList.add(goalsModel);
                        onGoalAdded.goalsAdding(goalsModelList);




                    }



                }



            }
        });



    }

    public interface OnGoalsAdded{
        void goalsAdding(List<GoalsModel> goalsModels);
    }

}
