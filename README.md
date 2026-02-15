# Pack Companion

### Description
**Pack Companion** is the ultimate sentinel mod for 1.12.2 designed to perform a runtime check for modpack stability. It automatically identifies mods that are outdated, problematic, or superseded by modern alternatives.

Pack Companion features a deep **Config Parser** that analyses problematic Config field values. It identifies settings that are known to cause crashes or severe performance issues.

The mod generates both **HTML/Markdown** reports (found in your `config/packcompanion/outputs` folder). These report provides a clean, readable breakdown of every flagged mod and config setting, complete with direct links to recommended fixes.

Upon joining the world, you will be provided a link to view the reports in your browser.

This mod is built against the [Cleanroom Mod List](https://cleanroommc.com/wiki/end-user-guide/preparing-your-modpack), a community-maintained database of deprecated 1.12.2 mods and their high-performance alternatives. Pack Companion ensures your modpack aligns with these modern standards, helping you transition away from legacy environments, into a more stable, Cleanroom-optimized environment.


---

### Template Usage
This mod is intended to be used with [BareBones Modpack Template](https://github.com/BareBonium/BareBones), which provides a pre-configured environment that includes:
* **Curated Optimizations:** A minimalistic list of major improvement, QoL and bugfix mods.
* **Standardized Configs:** Pre-configured config files for various mods, including Crash Assistant and Universal Tweaks.
* **Workflow Guide:** A step-by-step template to update old packs or start new ones with modern 1.12.2 standards.

---

### Master Lists
Pack Companion does not rely on static data. Instead, it utilizes a dynamic **Master List** system to provide the most current mod recommendations without requiring frequent mod updates:

* **GitHub Master Lists:** All data and recommendation logic is hosted on our [Master List Directory](https://github.com/BareBonium/PackCompanion-MasterList), which features:<br/><br/>
  * **Mod Master List**: containing definitions for problematic, outdated, or deprecated mods.
  * **Class Checking Master List**: A specialized list used to verify the presence of classes across classpath, or to match Hash values of Jar files
  * **Config Master List**: A list for auditing mod config fields.<br/><br/>

* **JSON Fetching:** On every startup, the mod fetches the latest live **JSON** releases.<br/><br/>
