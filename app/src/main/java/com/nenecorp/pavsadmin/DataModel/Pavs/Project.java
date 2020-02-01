package com.nenecorp.pavsadmin.DataModel.Pavs;

public class Project {
    private String projectId;
    private String projectName;
    private String projectType;
    private String projectDescription;
    private String projectStatus;
    private int teamSize;


    public Project(String projectId) {
        this.projectId = projectId;
        this.teamSize = 4;
    }

    public Project setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public Project setProjectType(String projectType) {
        this.projectType = projectType;
        return this;
    }

    public Project setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
        return this;
    }


    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Project setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public Project setTeamSize(int teamSize) {
        this.teamSize = teamSize;
        return this;
    }

    public String getProjectType() {
        return projectType;
    }

    public String getProjectDescription() {
        return projectDescription;
    }


}
