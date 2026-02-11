package com.barebonium.packcompanion.entries;

import com.barebonium.packcompanion.enumstates.Action;
import com.barebonium.packcompanion.enumstates.Status;
import com.barebonium.packcompanion.enumstates.Verification;

public class ClassCheckEntry extends AbstractModEntry {
    public Status status = Status.DEPRECATED;
    public Verification verification = Verification.CLASSLOADED;
    public String className;
    public String versionHash;
    public Action action = Action.REMOVE;
}
