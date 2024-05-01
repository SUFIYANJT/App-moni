package com.example.myapplication.model;

import com.example.myapplication.Support.FileData;

import java.util.ArrayList;

public class ReportCollection {
    private int activityId;
    private String activityName;
    private String activityDescription;
    private String reportFrom;
    private int report_user_id;
    private ArrayList<FileData> reports;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getReportFrom() {
        return reportFrom;
    }

    public void setReportFrom(String reportFrom) {
        this.reportFrom = reportFrom;
    }

    public int getReport_user_id() {
        return report_user_id;
    }

    public void setReport_user_id(int report_user_id) {
        this.report_user_id = report_user_id;
    }

    public ArrayList<FileData> getReports() {
        return reports;
    }

    public void setReports(ArrayList<FileData> reports) {
        this.reports = reports;
    }
}
