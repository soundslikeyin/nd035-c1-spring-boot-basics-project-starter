package com.udacity.jwdnd.course1.cloudstorage.model;

import org.springframework.stereotype.Component;

@Component
public class Tab {
    private static String currentTab = "files";

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        Tab.currentTab = currentTab;
    }
}
