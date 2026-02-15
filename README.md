# Pack Companion

### Description
**Pack Companion** is the ultimate sentinel mod for 1.12.2 designed to perform a runtime check for modpack stability. It automatically identifies mods that are outdated, problematic, or superseded by modern alternatives.

Pack Companion features a deep **Config Parser** that analyses problematic Config field values through Reflection. It identifies settings that are known to cause crashes or severe performance issues.

The mod generates both **HTML/Markdown** reports (found in your `config/packcompanion/output` folder). These reports provides a clean, readable breakdown of every flagged mod and config setting, complete with direct links to recommended fixes.

Upon joining the world, you will be provided a link to view the reports in your browser.

This mod is built against the [Cleanroom Wiki Mod List](https://cleanroommc.com/wiki/end-user-guide/preparing-your-modpack), a community-maintained database of deprecated 1.12.2 mods and their high-performance alternatives. Pack Companion ensures your modpack aligns with these modern standards, helping you transition away from legacy environments, into a more stable, Cleanroom-optimized environment.

---

### Template Usage
This mod is intended to be used with [BareBonesTemplatePack](https://github.com/Invadermonky/BareBonesTemplatePack), which provides a pre-configured environment that includes:
<ul>
  <li><b>Curated Optimizations:</b> A minimalistic list of major improvement, QoL and bugfix mods.</li>
  <li><b>Standardized Configs:</b> Pre-configured config files for various mods, including Crash Assistant and Universal Tweaks.</li>
  <li><b>Workflow Guide:</b> A step-by-step template to update old packs or start new ones with modern 1.12.2 standards.</li>
</ul>

---

### Master Lists
Pack Companion does not rely on static data. Instead, it utilizes a dynamic **Master List** system to provide the most current mod recommendations without requiring frequent mod updates:

<ul>
  <li>
    <b>GitHub Master Lists:</b> All data and recommendation logic is hosted on our <a href="https://github.com/AnasDevO/PackTemplateCompanion/tree/master/masterlists">Master List Directory</a>, which features:
    <ul>
      <li><b>Mod Master List:</b> Containing definitions for problematic, outdated, or deprecated mods.</li>
      <li><b>Class Checking Master List:</b> A specialized list used to verify the presence of classes across classpath, or to match Hash values of Jar files.</li>
      <li><b>Config Master List:</b> A list for auditing mod config fields.</li>
    </ul>
  </li>
  <li><b>JSON Fetching:</b> On every startup, the mod fetches the latest live <b>JSON</b> releases.</li>
</ul>
