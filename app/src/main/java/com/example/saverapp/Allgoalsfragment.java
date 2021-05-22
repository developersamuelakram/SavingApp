package com.example.saverapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saverapp.Adapter.AllGoalsAdapter;
import com.example.saverapp.MVVM.Viewmodel;
import com.example.saverapp.Model.GoalsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class Allgoalsfragment extends Fragment implements AllGoalsAdapter.OnGoalClicked {


    FloatingActionButton fab;
    NavController navController;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;

    RecyclerView recyclerView;
    AllGoalsAdapter mAdapter;
    Viewmodel viewModel;

    public Allgoalsfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_allgoalsfragment, container, false);
    }


    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AllGoalsAdapter(this);

        viewModel = new ViewModelProvider(getActivity()).get(Viewmodel.class);
        viewModel.GetAllGoals().observe(getViewLifecycleOwner(), new Observer<List<GoalsModel>>() {
            @Override
            public void onChanged(List<GoalsModel> goalsModels) {

                mAdapter.setGoalsModelList(goalsModels);
                recyclerView.setAdapter(mAdapter);


            }
        });
        fab = view.findViewById(R.id.fab);
        navController = Navigation.findNavController(view);
        pd = new ProgressDialog(getContext());



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   navController.navigate(R.id.action_allgoalsfragment_to_goalFragment);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Set New Goal");

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.input, (ViewGroup) getView(),
                        false);


// Set up the input
                final EditText goalName = (EditText) viewInflated.findViewById(R.id.goalName);
              final   EditText goalAmount = (EditText) viewInflated.findViewById(R.id.goalAmount);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String goalname = goalName.getText().toString();
                       String goalamount = goalAmount.getText().toString();



                       String userid = firebaseAuth.getCurrentUser().getUid();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("goalname", goalname);
                        hashMap.put("goalamount", goalamount);
                        hashMap.put("togo", "");
                        hashMap.put("currentAmount", 0);

                        pd.setMessage("Adding Goal");
                        pd.show();



                        firebaseFirestore.collection("Goal"+ userid).document().set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete( Task<Void> task) {
                               if (task.isSuccessful()) {

                                   pd.dismiss();
                                   Toast.makeText(getContext(), "Goal Added", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void goalClicked(int position, List<GoalsModel> goalsModels) {
        String goalname = goalsModels.get(position).getGoalname();
        String goalamount = goalsModels.get(position).getGoalamount();
        String goalid = goalsModels.get(position).getGoalid();

        AllgoalsfragmentDirections.ActionAllgoalsfragmentToGoalFragment action = AllgoalsfragmentDirections.actionAllgoalsfragmentToGoalFragment();
        action.setGoalAmount(goalamount);
        action.setGoalname(goalname);
        action.setGoalId(goalid);

        navController.navigate(action);




    }
}