package com.hackfirst.flamesof5.automate_cte_onboarding;

public class AccessRequest {
    private String empId;
    private String projectName;
    private String team;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "AccessRequest{" +
                "empId='" + empId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", team='" + team + '\'' +
                '}';
    }
}
