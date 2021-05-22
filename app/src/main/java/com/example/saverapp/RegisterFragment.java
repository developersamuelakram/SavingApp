package com.example.saverapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class RegisterFragment extends Fragment {



    TextInputEditText name, email, password;
    Switch aSwitch;
    Button regisBtn;
    FirebaseAuth firebaseAuth;
    String emailtext , passwordtext, nametext;
    FirebaseFirestore firestore;
    ProgressDialog pd;
    NavController navController;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }


    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        name = view.findViewById(R.id.nameRegister);
        email = view.findViewById(R.id.emailRegister);
        password = view.findViewById(R.id.passwordRegister);
        regisBtn = view.findViewById(R.id.RegisterButton);
        pd = new ProgressDialog(getContext());
        navController = Navigation.findNavController(view);

        aSwitch = view.findViewById(R.id.switchLogin);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_registerFragment_to_loginFragment);

            }
        });



        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nametext = name.getText().toString();
                emailtext = email.getText().toString();
                passwordtext = password.getText().toString();

                if (nametext.isEmpty()) {

                    name.setError("Enter Name");
                }
                if (emailtext.isEmpty()) {
                    email.setError("Enter Email");
                }

                if (passwordtext.isEmpty()) {
                    password.setError("Enter Password");

                }
                if (passwordtext.length() < 4) {
                    password.setError("Password Length must be 5 or more");

                } else {

                    registerUser(nametext, emailtext, passwordtext);

                }
            }
        });
    }

    private void registerUser(String nametext, String emailtext, String passwordtext) {

        pd.setMessage("Registering");
        pd.show();




        firebaseAuth.createUserWithEmailAndPassword(emailtext, passwordtext).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String userid = firebaseAuth.getCurrentUser().getUid();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("username", nametext);
                    hashMap.put("email", emailtext);

                    pd.dismiss();

                    firestore.collection("Users").document(userid).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( Task<Void> task) {

                        }
                    });

                    Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();

                    navController.navigate(R.id.action_registerFragment_to_loginFragment);



                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

                Log.d("TAG", "onFailure: " + e);

            }
        });





    }
}