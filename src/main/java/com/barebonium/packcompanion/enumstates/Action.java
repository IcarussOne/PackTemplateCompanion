package com.barebonium.packcompanion.enumstates;

import com.barebonium.packcompanion.entries.AbstractModEntry;

import java.util.function.BiFunction;

public enum Action {
    REMOVE((entry, modName) -> "Remove " + modName),
    DOWNGRADE((modEntry, modName) -> "Downgrade to version " + modEntry.replacementModVersion),
    UPGRADE((modEntry, modName) -> "Upgrade to version " + modEntry.replacementModVersion),
    REPLACE(
            (modEntry, modName) -> String.format("Replace with [%s](%s)", modEntry.replacementModName, modEntry.replacementModLink),
            (modEntry, modName) -> String.format("<p>Replace with <a href=\"%s\">%s</a></p>", modEntry.replacementModName, modEntry.replacementModLink)
    ),
    INCLUDE((modEntry, modName) -> "Check mod compatibility"),
    INFO((modEntry, modName) -> "No Action Needed");

    private final BiFunction<AbstractModEntry, String, String> markdownOutput;
    private final BiFunction<AbstractModEntry, String, String> htmlOutput;

    Action(BiFunction<AbstractModEntry, String, String> markdownOutput, BiFunction<AbstractModEntry, String, String> htmlOutput) {
        this.markdownOutput = markdownOutput;
        this.htmlOutput = htmlOutput;
    }

    Action(BiFunction<AbstractModEntry, String, String> standardOutput) {
        this(standardOutput, standardOutput);
    }

    public String getMarkdownActionString(AbstractModEntry entry, String modName) {
        return this.markdownOutput.apply(entry, modName);
    }

    public String getHtmlActionString(AbstractModEntry entry, String modName) {
        if(this.markdownOutput.equals(this.htmlOutput)) {
            return "<p>" + this.getMarkdownActionString(entry, modName) + "</p>";
        }
        return this.htmlOutput.apply(entry, modName);
    }
}
