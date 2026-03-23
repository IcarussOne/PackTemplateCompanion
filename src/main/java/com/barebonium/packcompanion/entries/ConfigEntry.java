package com.barebonium.packcompanion.entries;

import java.util.List;

public class ConfigEntry {
    public String modId;
    public String version;
    public boolean isMinVersion;
    public boolean isMaxVersion;
    public List<ConfigSetting> settings;
}
