package com.nenecorp.pavsadmin.DataModel;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nenecorp.pavsadmin.DataModel.Pavs.Project;
import com.nenecorp.pavsadmin.Utility.Resources.Dictionary;

import java.util.ArrayList;

public class PavsDB {
    private ArrayList<Project> completedProjects, projectRequests, approvedProjects;
    private DataSnapshot pavsDatabase;
    private ArrayList<DataListener> dataListeners;


    public interface DatabaseInterface {
        void onLoaded(PavsDB pavsDB);
    }

    public ArrayList<Project> getCompletedProjects() {
        return completedProjects;
    }

    public ArrayList<Project> getProjectRequests() {
        return projectRequests;
    }

    public ArrayList<Project> getApprovedProjects() {
        return approvedProjects;
    }

    public PavsDB(DatabaseInterface databaseInterface) {
        init();
        final Handler handler = new Handler();
        final int delay = 5;
        handler.postDelayed(new Runnable() {
            public void run() {
                if (isLoaded()) {
                    databaseInterface.onLoaded(PavsDB.this);
                } else {
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);
    }

    private boolean isLoaded() {
        return pavsDatabase != null;
    }

    private void init() {
        dataListeners = new ArrayList<>();
        completedProjects = new ArrayList<>();
        projectRequests = new ArrayList<>();
        approvedProjects = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pavsDatabase = dataSnapshot;
                completedProjects = new ArrayList<>();
                projectRequests = new ArrayList<>();
                getSoloProjects(dataSnapshot.child(Dictionary.PROJECTS).child(Dictionary.SOLO_PROJECT));
                getGroupProjects(dataSnapshot.child(Dictionary.PROJECTS).child(Dictionary.TEAM_PROJECT));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference().addValueEventListener(eventListener);
    }

    private void getGroupProjects(DataSnapshot child) {
        for (DataSnapshot yr : child.getChildren()) {
            for (DataSnapshot projectSnapshot : yr.getChildren()) {
                String projectId = projectSnapshot.getKey();
                String projectName = projectSnapshot.child(Dictionary.projectName).getValue(String.class);
                String projectType = projectSnapshot.child(Dictionary.projectType).getValue(String.class);
                String projectDescription = projectSnapshot.child(Dictionary.projectDescription).getValue(String.class);
                String projectStatus = projectSnapshot.child(Dictionary.projectStatus).getValue(String.class);
                Project x = new Project(projectId).setProjectName(projectName)
                        .setProjectType(projectType)
                        .setProjectDescription(projectDescription)
                        .setProjectStatus(projectStatus);
                switch (x.getProjectStatus()) {
                    case Dictionary.COMPLETED:
                        completedProjects.add(x);
                        break;
                    case Dictionary.APPROVED:
                        approvedProjects.add(x);
                        break;
                    case Dictionary.REQUESTING:
                        projectRequests.add(x);
                        break;
                }
            }
        }
    }

    private void getSoloProjects(DataSnapshot child) {
        for (DataSnapshot yr : child.getChildren()) {
            for (DataSnapshot projectSnapshot : yr.getChildren()) {
                String projectId = projectSnapshot.getKey();
                String projectName = projectSnapshot.child(Dictionary.projectName).getValue(String.class);
                String projectType = projectSnapshot.child(Dictionary.projectType).getValue(String.class);
                String projectDescription = projectSnapshot.child(Dictionary.projectDescription).getValue(String.class);
                String projectStatus = projectSnapshot.child(Dictionary.projectStatus).getValue(String.class);
                Project x = new Project(projectId).setProjectName(projectName)
                        .setProjectType(projectType)
                        .setProjectDescription(projectDescription)
                        .setProjectStatus(projectStatus);
                switch (x.getProjectStatus()) {
                    case Dictionary.COMPLETED:
                        completedProjects.add(x);
                        break;
                    case Dictionary.APPROVED:
                        approvedProjects.add(x);
                        break;
                    case Dictionary.REQUESTING:
                        projectRequests.add(x);
                        break;
                }
            }
        }
    }

    public interface DataListener {
        void newRequests();

        void newSubmission();

    }

    public void addDataListener(DataListener dataListener) {
        dataListeners.add(dataListener);
    }

    public String getProjectYear(String id) {
        String i[] = id.split("-");
        return i[1];
    }

    public void denyProject(Project z, ResultListener listener) {
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child(Dictionary.PROJECTS);
        if (z.getProjectType().equals(Dictionary.TEAM_PROJECT)) {
            r = r.child(Dictionary.TEAM_PROJECT).child(getProjectYear(z.getProjectId())).child(z.getProjectId());
        } else {
            r = r.child(Dictionary.SOLO_PROJECT).child(getProjectYear(z.getProjectId())).child(z.getProjectId());
        }
        r.child(Dictionary.projectStatus).setValue(Dictionary.DENIED).addOnSuccessListener(aVoid -> {
            completedProjects.remove(z);
            listener.onSuccess();
            for (DataListener listener1 : dataListeners) {
                listener1.newRequests();
            }

        });
    }

    public void approveProject(Project z, ResultListener listener) {
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child(Dictionary.PROJECTS);
        if (z.getProjectType().equals(Dictionary.TEAM_PROJECT)) {
            r = r.child(Dictionary.TEAM_PROJECT).child(getProjectYear(z.getProjectId())).child(z.getProjectId());
        } else {
            r = r.child(Dictionary.SOLO_PROJECT).child(getProjectYear(z.getProjectId())).child(z.getProjectId());
        }
        r.child(Dictionary.projectStatus).setValue(Dictionary.APPROVED).addOnSuccessListener(aVoid -> {
            completedProjects.remove(z);
            listener.onSuccess();
            for (DataListener listener1 : dataListeners) {
                listener1.newRequests();
            }

        });
    }

    public void markedProject(Project z, ResultListener listener) {
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child(Dictionary.PROJECTS);
        if (z.getProjectType().equals(Dictionary.TEAM_PROJECT)) {
            r = r.child(Dictionary.TEAM_PROJECT).child(getProjectYear(z.getProjectId())).child(z.getProjectId());
        } else {
            r = r.child(Dictionary.SOLO_PROJECT).child(getProjectYear(z.getProjectId())).child(z.getProjectId());
        }
        r.child(Dictionary.projectStatus).setValue(Dictionary.MARKED).addOnSuccessListener(aVoid -> {
            completedProjects.remove(z);
            listener.onSuccess();
            for (DataListener listener1 : dataListeners) {
                listener1.newSubmission();
            }

        });


    }

    public interface ResultListener {
        void onSuccess();
    }

}

