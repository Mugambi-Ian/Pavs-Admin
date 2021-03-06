package com.nenecorp.pavsadmin.Interface.StartUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.nenecorp.pavsadmin.DataModel.PavsDB;
import com.nenecorp.pavsadmin.DataModel.PavsDBController;
import com.nenecorp.pavsadmin.Interface.AdminUi.Home;
import com.nenecorp.pavsadmin.R;
import com.nenecorp.pavsadmin.Utility.Resources.Animator;

public class SignUp extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "Sign_In";
    private GoogleSignInClient mGoogleSignInClient;
    private View signInProgress;
    private PavsDB pavsDB;

    @Override
    public void finish() {
        boolean start = FirebaseAuth.getInstance().getCurrentUser() != null;
        if (pavsDB != null && start) {
            overridePendingTransition(0, 0);
            startActivity(new Intent(this, Home.class));
            overridePendingTransition(0, 0);
        }
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (PavsDBController.isLoaded()) {
            pavsDB = PavsDBController.getDatabase();
            loadContent();
        } else {
            new PavsDBController(database -> {
                pavsDB = database;
                loadContent();
            });
        }

    }

    private void loadContent() {
        signInProgress = findViewById(R.id.ASU_progressBar);
        findViewById(R.id.ASU_btnSignIn).setOnClickListener(v -> {
            Animator.OnClick(v, v1 -> signIn());
            v.setEnabled(false);
            signInProgress.setVisibility(View.VISIBLE);
        });
    }

    private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Log.w(TAG, "Google sign in failed", e);
        }

    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        mGoogleSignInClient.signOut();
        String[] email = acct.getEmail().split("@");
        String domain = email[1].toLowerCase();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    signInProgress.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        if (domain.equals("kabarak.ac.ke")) {
                            finish();
                        } else {
                            findViewById(R.id.ASU_btnSignIn).setEnabled(true);
                            Toast.makeText(this, "Exclusively Kabarak", Toast.LENGTH_LONG).show();
                        }
                    }

                });

    }
}
