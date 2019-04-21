package org.xujin.moss.model.trace;

import java.io.Serializable;
import java.util.List;

public class ApplicationsData implements Serializable {
    private List<Application> applications;

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}