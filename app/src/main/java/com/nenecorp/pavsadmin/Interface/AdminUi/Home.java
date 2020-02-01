package com.nenecorp.pavsadmin.Interface.AdminUi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.nenecorp.pavsadmin.DataModel.PavsDB;
import com.nenecorp.pavsadmin.DataModel.PavsDBController;
import com.nenecorp.pavsadmin.R;
import com.nenecorp.pavsadmin.Utility.Resources.Animator;
import com.nenecorp.pavsadmin.Utility.Resources.Cache;

public class Home extends AppCompatActivity {
    private PavsDB pavsDB;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize() {
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
        ((TextView) findViewById(R.id.AH_txtAdmNumber)).setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        findViewById(R.id.AH_btnLogOut).setOnClickListener(v -> Animator.OnClick(v, v1 -> logOut()));
        findViewById(R.id.AH_btnCompletedProject).setOnClickListener(v -> Animator.OnClick(v, v12 -> {
            if (pavsDB.getCompletedProjects().size() != 0) {
                overridePendingTransition(0, 0);
                startActivity(new Intent(Home.this, DownloadProjects.class));
                overridePendingTransition(0, 0);
            } else {
                Toast.makeText(Home.this, "You haven't received any new requests.", Toast.LENGTH_SHORT).show();
            }
        }));
        findViewById(R.id.AH_btnProjectRequests).setOnClickListener(v -> Animator.OnClick(v, v13 -> {
            if (pavsDB.getProjectRequests().size() != 0) {
                overridePendingTransition(0, 0);
                startActivity(new Intent(Home.this, ApproveProjects.class));
                overridePendingTransition(0, 0);
            } else {
                Toast.makeText(Home.this, "You haven't received any new submissions.", Toast.LENGTH_SHORT).show();
            }
        }));

    }

    private void logOut() {
    }

    @Override
    public void finish() {
        super.finish();
        Cache.setHome(null);
        new PavsDBController(database -> {
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        new PavsDBController(database -> {
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Cache.setHome(this);
        initialize();
    }

}
