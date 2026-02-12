package com.barebonium.packcompanion.utils.helpers;

import com.barebonium.packcompanion.PackCompanion;
import com.barebonium.packcompanion.config.ConfigHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderException;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.common.versioning.VersionRange;

import javax.annotation.Nullable;
import java.io.File;

public class ModHelper {
    public static boolean isModLoaded(String modId) {
        boolean loaded = Loader.isModLoaded(modId);
        if(ConfigHandler.debugMode){
            PackCompanion.LOGGER.info("Mod {} Load status: {} using the following jar: {}",modId, loaded, getModSource(modId));
        }
        return loaded;
    }

    public static boolean isModLoaded(String modId, @Nullable String version) {
        return isModLoaded(modId, version, true, false);
    }

    public static boolean isModLoaded(String modId, @Nullable String version, boolean isMinVersion, boolean isMaxVersion) {
        if(version == null) {
            return isModLoaded(modId);
        } else {
            boolean loaded = Loader.isModLoaded(modId) && isSpecifiedVersion(modId, version, isMinVersion, isMaxVersion);
            if(ConfigHandler.debugMode){
                PackCompanion.LOGGER.info("Mod {} Load status: {} using the following jar: {}",modId, loaded, getModSource(modId));
                PackCompanion.LOGGER.info("Specified version: {}, whereas the actual version is {}", version, Loader.instance().getIndexedModList().get(modId).getProcessedVersion());
            }
            return Loader.isModLoaded(modId) && isSpecifiedVersion(modId, version, isMinVersion, isMaxVersion);
        }
    }

    public static String getModName(String modId) {
        ModContainer container = Loader.instance().getIndexedModList().get(modId);
        if (container != null) {
            String name = container.getName();
            if (!name.isEmpty()) {
                return name;
            }
            else{
                return modId;
            }
        }
        return modId;
    }
    public static File getModSource(String modId) {
        ModContainer container = Loader.instance().getIndexedModList().get(modId);
        if (container != null) {
            return container.getSource();
        }
        return null;
    }
    public static boolean isClassLoaded(String className) {
        try {
            Class.forName(className);
            if (ConfigHandler.debugMode) {
                PackCompanion.LOGGER.error("Found loaded class: {}", className);
            }
            return true;
        } catch(ClassNotFoundException e) {
            PackCompanion.LOGGER.error("Failed to load class {}: {}", className, e.getMessage());
            return false;
        }
    }


    public static boolean isSpecifiedVersion(String modId, @Nullable String version, boolean isMinVersion, boolean isMaxVersion) {
        if (version == null)
            return true;

        boolean match = true;
        ModContainer container = Loader.instance().getIndexedModList().get(modId);
        if (container != null) {
            try {
                VersionRange versionRange = VersionParser.parseRange(getVersionString(version, isMinVersion, isMaxVersion));
                match = versionRange.containsVersion(container.getProcessedVersion());
            } catch (LoaderException ignored) {
            }
        }
        return match;
    }

    private static String getVersionString(String version) {
        return getVersionString(version, true, false);
    }

    private static String getVersionString(String version, boolean isMinVersion, boolean isMaxVersion) {
        String versionStr = "";
        versionStr += isMinVersion ? "[" : "(";
        versionStr += isMaxVersion ? "," : "";
        versionStr += version;
        versionStr += isMinVersion ? "," : "";
        versionStr += isMaxVersion ? "]" : ")";
        return versionStr;
    }
}