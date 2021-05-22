package com.example.saverapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saverapp.Adapter.TransAdapter;
import com.example.saverapp.Model.GoalsModel;
import com.example.saverapp.Model.TransModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GoalFragment extends Fragment {


    Button withdraw, deposit;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    TextView goalInfo, goalPercent;
    TextView toGo;
    int totalGoalAmount = 0;
    String goalid;
    ProgressDialog pd;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TransAdapter mAdapter;
    List<TransModel> transModelList;

    public GoalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goal, container, false);
    }


    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(getContext());

        progressBar = view.findViewById(R.id.pbGoal);


//        transactions and shit


        transModelList = new ArrayList<>();

        goalInfo = view.findViewById(R.id.GoalInfo);
        toGo = view.findViewById(R.id.toGo);
        goalPercent = view.findViewById(R.id.goalprogress);
        deposit = view.findViewById(R.id.Deposit);
        withdraw = view.findViewById(R.id.Withdraw);
        recyclerView = view.findViewById(R.id.recViewTrans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String goalAmount = GoalFragmentArgs.fromBundle(getArguments()).getGoalAmount();
        String goalName = GoalFragmentArgs.fromBundle(getArguments()).getGoalname();
        goalid = GoalFragmentArgs.fromBundle(getArguments()).getGoalId();



//        trans and shit

        firestore.collection("Trans"+goalid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value,  FirebaseFirestoreException error) {

                transModelList.clear();
                if (value!=null) {

                    for (DocumentSnapshot ds: value.getDocuments()){

                        TransModel transModel = ds.toObject(TransModel.class);

                        mAdapter = new TransAdapter();
                        transModelList.add(transModel);
                        mAdapter.setTransModelList(transModelList);
                        recyclerView.setAdapter(mAdapter);


                    }

                }

            }
        });


        goalInfo.setText(goalName + " is "+goalAmount);

        totalGoalAmount = Integer.parseInt(goalAmount);


        progressBar.setMax(totalGoalAmount);

        String userid = firebaseAuth.getCurrentUser().getUid();
        firestore.collection("Goal"+userid).document(goalid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete( Task<DocumentSnapshot> task) {


                if (task.isSuccessful()) {



                    GoalsModel goalsModel = task.getResult().toObject(GoalsModel.class);
                    int currentPresentamount = goalsModel.getCurrentAmount();



                    progressBar.setProgress(currentPresentamount);


                    if (totalGoalAmount == currentPresentamount ) {

                        toGo.setText( "Goal Reached");


                    } else {

                        toGo.setText( "More to go "+String.valueOf(totalGoalAmount - currentPresentamount));


                    }


                    int percent = ( currentPresentamount * 100)/totalGoalAmount;
                    goalPercent.setText(String.valueOf(percent) + "%");

                }


            }
        });


        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Withdraw");

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.withdrawinput, (ViewGroup) getView(),
                        false);

                final EditText etWithDraw = (EditText) viewInflated.findViewById(R.id.etWithdraw);


                builder.setView(viewInflated);

                builder.setPositiveButton("Withdraw", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        pd.setMessage("Withdrawing");
                        pd.show();
                        int amountwithdraw = Integer.parseInt(etWithDraw.getText().toString());

                        if (amountwithdraw > totalGoalAmount) {
                            pd.dismiss();

                            Toast.makeText(getContext(), "Withdrawal must be lower the totalGoalAmount", Toast.LENGTH_SHORT).show();
                        }


                        String userid = firebaseAuth.getCurrentUser().getUid();
                        firestore.collection("Goal"+userid).document(goalid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()){

                                    GoalsModel goalsModel = task.getResult().toObject(GoalsModel.class);
                                    int currentAmount = goalsModel.getCurrentAmount();


                                    if (currentAmount == 0) {

                                        Toast.makeText(getContext(), "Cant WithDrawShit", Toast.LENGTH_SHORT).show();

                                    } else if (currentAmount > 0) {

                                        currentAmount -= amountwithdraw ;


                                        progressBar.setProgress(currentAmount);



                                        toGo.setText( "More to go "+String.valueOf(totalGoalAmount - currentAmount));

                                        int percent = ( currentAmount * 100)/totalGoalAmount;
                                        goalPercent.setText(String.valueOf(percent) + "%");


                                        pd.dismiss();


                                        // transactions records


                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                        LocalDateTime now = LocalDateTime.now();
                                        String time = dtf.format(now);

                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("trans", "Withdrawal of "+ amountwithdraw);
                                        firestore.collection("Trans"+goalid).document(time).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(Task<Void> task) {

                                            }
                                        });




                                        firestore.collection("Goal"+userid).document(goalid).update("currentAmount", currentAmount);

                                    }






                                }




                            }
                        });







                    }
                });


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                });

                builder.show();



            }
        });

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Deposit");

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.depositinput, (ViewGroup) getView(),
                        false);

                final EditText etDeposit = (EditText) viewInflated.findViewById(R.id.etDeposit);


                builder.setView(viewInflated);

                builder.setPositiveButton("Deposit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int amountdeposited = Integer.parseInt(etDeposit.getText().toString());

                        String userid = firebaseAuth.getCurrentUser().getUid();
                        pd.setMessage("Depositing");
                        pd.show();

                        if (amountdeposited <= totalGoalAmount){



                            //updating the currentamount

                            firestore.collection("Goal"+userid).document(goalid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onComplete(Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()){

                                        GoalsModel goalsModel = task.getResult().toObject(GoalsModel.class);
                                        int currentPresentamount = goalsModel.getCurrentAmount();

                                        currentPresentamount +=amountdeposited;


                                        progressBar.setProgress(currentPresentamount);

                                        if (totalGoalAmount == currentPresentamount) {

                                            toGo.setText( "Goal Reached");


                                        } else {

                                            toGo.setText( "More to go "+String.valueOf(totalGoalAmount - currentPresentamount));

                                        }






                                        int percent = ( currentPresentamount * 100)/totalGoalAmount;
                                        goalPercent.setText(String.valueOf(percent) + "%");


                                        firestore.collection("Goal"+userid).document(goalid).update("currentAmount", currentPresentamount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(Task<Void> task) {

                                                if (task.isSuccessful()){

                                                    pd.dismiss();
                                                    Toast.makeText(getContext(), "Deposited", Toast.LENGTH_SHORT).show();




                                                }

                                            }
                                        });


                                        // transactions records


                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                        LocalDateTime now = LocalDateTime.now();
                                        String time = dtf.format(now);

                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("trans", "Deposited "+ amountdeposited);
                                        firestore.collection("Trans"+goalid).document(time).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(Task<Void> task) {

                                            }
                                        });


                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(getContext(), "Deposited Cant be more than goal's amount", Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });


                                    }


                                }
                            });




                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                });

                builder.show();

            }
        });


    }
}