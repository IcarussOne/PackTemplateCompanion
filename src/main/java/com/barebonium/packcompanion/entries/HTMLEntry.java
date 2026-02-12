package com.barebonium.packcompanion.entries;

import com.barebonium.packcompanion.enumstates.Action;
import com.barebonium.packcompanion.enumstates.Status;

import java.util.List;

public class HTMLEntry {
    public String modName;
    public Status status;
    public String actualVersion;
    public String replacementModName;
    public String replacementModVersion;
    public String replacementModLink;
    public Action action;
    public String actionMessage;
    public boolean isMinVersion;
    public boolean isMaxVersion;
    public List<ModPatchEntry> patchList;
    public String message;
    public boolean isCleanroom;

    public HTMLEntry(String modName, ModEntry entry) {
        this.modName = modName;
        this.status = entry.status;
        this.actualVersion = entry.version;
        this.replacementModName = entry.replacementModName;
        this.replacementModLink = entry.replacementModLink;
        this.action = entry.action;
        this.actionMessage = entry.action.getHtmlActionString(entry, modName);
        this.isMinVersion = entry.isMinVersion;
        this.isMaxVersion = entry.isMaxVersion;
        this.replacementModVersion = entry.replacementModVersion;
        this.patchList = entry.patchList;
        this.message = entry.message;
        this.isCleanroom = entry.isCleanroom;
    }

    public HTMLEntry(String modName, ClassCheckEntry entry) {
        this.modName = modName;
        this.status = entry.status;
        this.actualVersion = null;
        this.replacementModName = entry.replacementModName;
        this.replacementModLink = entry.replacementModLink;
        this.action = entry.action;
        this.actionMessage = entry.action.getHtmlActionString(entry, modName);
        this.isMinVersion = false;
        this.isMaxVersion = false;
        this.replacementModVersion = entry.replacementModVersion;
        this.patchList = entry.patchList;
        this.message = entry.message;
        this.isCleanroom = entry.isCleanroom;
    }

}
