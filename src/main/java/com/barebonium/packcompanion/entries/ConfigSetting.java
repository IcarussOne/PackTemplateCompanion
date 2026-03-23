package com.barebonium.packcompanion.entries;

import com.barebonium.packcompanion.enumstates.DependencyMode;

import java.util.List;

public class ConfigSetting {
    public String name;
    public String field;
    public String type;
    public String value;
    public Boolean shouldMatch;
    public String message;
    public DependencyMode dependencyMode = DependencyMode.AND;
    public List<ModDependency> dependencies;
}
