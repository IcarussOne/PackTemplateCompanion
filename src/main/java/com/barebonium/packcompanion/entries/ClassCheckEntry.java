package com.barebonium.packcompanion.entries;

import com.barebonium.packcompanion.enumstates.Action;
import com.barebonium.packcompanion.enumstates.Status;
import com.barebonium.packcompanion.enumstates.Verification;

import javax.annotation.Nullable;
import java.util.List;

public class ClassCheckEntry {
    public String modId;
    public Status status = Status.DEPRECATED;
    public Verification verification = Verification.CLASSLOADED;
    public String className;
    public String versionHash;
    public Action action = Action.REMOVE;
    public String replacementModName;
    public String replacementModLink;
    public String replacementModVersion;
    public String message;
    public @Nullable List<ModPatchEntry> patchList;
    public boolean isCleanroom = false;
}
