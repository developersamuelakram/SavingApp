package com.example.saverapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {



    TextInputEditText email, password;
    Switch aSwitch;
    Button loginBtn;
    FirebaseAuth firebaseAuth;
    String emailtext , passwordtext;
    FirebaseFirestore firestore;
    ProgressDialog pd;
    NavController navController;



    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.emailLogin);
        password = view.findViewById(R.id.passwordLogin);
        loginBtn = view.findViewById(R.id.loginButton);
        pd = new ProgressDialog(getContext());
        navController = Navigation.findNavController(view);

        aSwitch = view.findViewById(R.id.switchReg);

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_loginFragment_to_registerFragment);

            }
        });






        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailtext = email.getText().toString();
                passwordtext = password.getText().toString();

                if (emailtext.isEmpty()) {
                    email.setError("Enter Email");
                }

                if (passwordtext.isEmpty()){
                    password.setError("Enter Password");

                }
                if (passwordtext.length() < 4) {
                    password.setError("Password Length must be 5 or more");

                } else {

                    LoginShit(emailtext, passwordtext);

                }




            }
        });


    }

    private void LoginShit(String emailtext, String passwordtext) {

        pd.setMessage("Logging In");
        pd.show();


        firebaseAuth.signInWithEmailAndPassword(emailtext, passwordtext).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {

                pd.dismiss();
                navController.navigate(R.id.action_loginFragment_to_allgoalsfragment);
                Toast.makeText(getContext(), "Logged In", Toast.LENGTH_SHORT).show();


            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser()!=null) {
            navController.navigate(R.id.action_loginFragment_to_allgoalsfragment);



        }
    }

}