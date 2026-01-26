package com.barebonium.packcompanion.utils;

import com.barebonium.packcompanion.enumstates.Action;
import com.barebonium.packcompanion.enumstates.Status;

public class ModEntry {
    public String modId;
    public Status status;
    public String version;
    public String replacementModName;
    public String replacementModVersion;
    public String replacementModLink;
    public Action action;
    public boolean isMinVersion;
    public boolean isMaxVersion;
}
