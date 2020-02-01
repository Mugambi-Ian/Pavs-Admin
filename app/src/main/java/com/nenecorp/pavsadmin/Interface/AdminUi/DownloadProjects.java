package com.nenecorp.pavsadmin.Interface.AdminUi;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.nenecorp.pavsadmin.DataModel.Pavs.Project;
import com.nenecorp.pavsadmin.DataModel.PavsDB;
import com.nenecorp.pavsadmin.DataModel.PavsDBController;
import com.nenecorp.pavsadmin.R;
import com.nenecorp.pavsadmin.Utility.Resources.Animator;
import com.nenecorp.pavsadmin.Utility.Resources.Dictionary;

import java.util.ArrayList;

public class DownloadProjects extends AppCompatActivity {
    private ArrayList<Project> projects;
    private PavsDB pavsDB;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_projects);
        initialize();
    }

    private void loadContent() {
        projects = pavsDB.getCompletedProjects();
        manageProject(projects.get(0));
        pavsDB.addDataListener(new PavsDB.DataListener() {
            @Override
            public void newRequests() {
                projects = pavsDB.getCompletedProjects();
                if (projects.size() != 0) {
                    manageProject(projects.get(0));
                } else {
                    Toast.makeText(DownloadProjects.this, "Those were all the submissions received.", Toast.LENGTH_SHORT).show();
                    overridePendingTransition(0, 0);
                    finish();
                }


            }

            @Override
            public void newSubmission() {

            }
        });
    }

    private void manageProject(Project x) {
        Drawable img;
        if (x.getProjectType().equals(Dictionary.TEAM_PROJECT)) {
            img = ContextCompat.getDrawable(this, R.drawable.teamwork);
        } else {
            img = ContextCompat.getDrawable(this, R.drawable.solo);
        }
        Glide.with(this).load(img).into((ImageView) findViewById(R.id.PIP_imgProject));
        ((TextView) findViewById(R.id.PIP_projectDescription)).setText(x.getProjectDescription());
        ((TextView) findViewById(R.id.PIP_projectName)).setText(x.getProjectName());
        ((TextView) findViewById(R.id.PIP_projectType)).setText(x.getProjectStatus());
        findViewById(R.id.PIP_btnDownLoad).setVisibility(View.VISIBLE);
        findViewById(R.id.PIP_btnDownLoad).setOnClickListener(v -> Animator.OnClick(v, v1 -> pavsDB.markedProject(x, () -> {
            overridePendingTransition(0, 0);
            startActivity(new Intent(this, DownloadProjects.class));
            finish();
        })));
    }

}
