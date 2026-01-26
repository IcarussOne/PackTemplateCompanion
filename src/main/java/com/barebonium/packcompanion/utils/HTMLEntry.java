package com.barebonium.packcompanion.utils;

import com.barebonium.packcompanion.enumstates.Status;
import com.barebonium.packcompanion.enumstates.Action;

public class HTMLEntry {
    public String modName;
    public Status status;
    public String actualVersion;
    public String replacementModName;
    public String replacementModVersion;
    public String replacementModLink;
    public Action action;
    public boolean isMinVersion;
    public boolean isMaxVersion;

    public HTMLEntry(String modName, Status status, String actualVersion, String replacementModName, String replacementModLink, Action action, boolean isMinVersion, boolean isMaxVersion, String replacementModVersion) {
        this.modName = modName;
        this.status = status;
        this.actualVersion = actualVersion;
        this.replacementModName = replacementModName;
        this.replacementModLink = replacementModLink;
        this.action = action;
        this.isMinVersion = isMinVersion;
        this.isMaxVersion = isMaxVersion;
        this.replacementModVersion = replacementModVersion;
    }
}
