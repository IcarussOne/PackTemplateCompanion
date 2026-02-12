package com.barebonium.packcompanion.entries;

public class ModDependency {
    public String modId;
    public String version = null;
    public boolean isMinVersion = true;
    public boolean isMaxVersion = false;
    public String className = "";
    public boolean classLoaded = false;
}
