package com.hackfirst.flamesof5.automate_cte_onboarding;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;

@Entity
public class Template {

    @Id
    private Long id;
    private String teamName;
    private String accessRequests;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getRequestAccessJson() {
        return accessRequests;
    }

    public void setRequestAccessJson(String requestAccessJson) {
        this.accessRequests = requestAccessJson;
    }
}
