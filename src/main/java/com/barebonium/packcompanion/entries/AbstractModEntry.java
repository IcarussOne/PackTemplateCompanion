package com.barebonium.packcompanion.entries;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractModEntry {
    public String modId;
    public String replacementModName;
    public String replacementModLink;
    public String replacementModVersion;
    public String message = "";
    public @Nullable List<ModPatchEntry> patchList;
    public boolean isCleanroom = false;
}
