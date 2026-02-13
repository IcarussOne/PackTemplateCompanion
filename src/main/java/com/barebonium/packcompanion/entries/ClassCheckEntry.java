package com.barebonium.packcompanion.entries;

import com.barebonium.packcompanion.enumstates.Verification;

public class ClassCheckEntry extends AbstractModEntry {
    public Verification verification = Verification.CLASSLOADED;
    public String className;
    public String versionHash;
}
