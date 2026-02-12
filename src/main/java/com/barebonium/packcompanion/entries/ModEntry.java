package com.barebonium.packcompanion.entries;

import com.barebonium.packcompanion.enumstates.Action;
import com.barebonium.packcompanion.enumstates.Status;

import javax.annotation.Nullable;

public class ModEntry extends AbstractModEntry{
    /** The mod version string. */
    public @Nullable String version;

    /** Whether this version is the earliest version accepted. */
    public boolean isMinVersion = false;

    /** Whether this mod version is the oldest version accepted. */
    public boolean isMaxVersion = false;

    /**
     * Whether the version check should be inclusive or exclusive.
     * <p>
     *     Inclusive check will return true if the mod version is within the version range.
     *     Exclusive check will return true if the mod version is <b>not</b> within the version range.
     * <p>
     *     This is useful to find any ranges outside preset limits, such as any Thaumic Wonders version
     *     that is not Thaumic Wonders Unofficial (starts at 2.0.0).
     * <pre>
     * {
     *     "modId": "thaumicwonders",
     *     "version": "2.0.0",
     *     "isMinVersion": true,
     *     "isMaxVersion": false,
     *     "isVersionExclusive": true
     * }
     * </pre>
     * <p>
     *     The logic feels a little backwards, but it's focused on HTML generation and not mod integration.
     */
    public boolean isVersionExclusive;

    /** The status of any mod that is not found in the specified version range.
     * <br>
     * Values include:
     * <li> DEPRECATED - used when a mod is forked or superseded and should be replaced </li>
     * <li> PROBLEMATIC - used when a mod has major bugs and should be removed or downgraded </li>
     */
    public Status status = Status.DEPRECATED;

    /**
     * The recommended action to fix this issue.
     * <li>REMOVE - Remove the mod without replacement </li>
     * <li>DOWNGRADE - Roll back to a previous version of the mod </li>
     * <li>UPGRADE - Upgrade the mod to a new version </li>
     * <li>REPLACE - Replace the mod with a different mod </li>
     * <li>INCLUDE - Include an additional mod (such as patch mods) </li>
     */
    public Action action = Action.REMOVE;
}
