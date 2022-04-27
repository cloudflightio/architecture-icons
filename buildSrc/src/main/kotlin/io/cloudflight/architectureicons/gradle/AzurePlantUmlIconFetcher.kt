package io.cloudflight.architectureicons.gradle

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.net.URL


class AzurePlantUmlIconFetcher {

    private val url = "https://raw.githubusercontent.com/plantuml-stdlib/Azure-PlantUML/master/AzureSymbols.md"
    private val sourceUrl = "https://github.com/plantuml-stdlib/Azure-PlantUML/blob/master/AzureSymbols.md"
    private val rootUrl = "https://raw.githubusercontent.com/plantuml-stdlib/Azure-PlantUML/master/dist"

    private val LICENSE_TEXT = listOf(
        "Microsoft permits the use of these icons in architectural diagrams, training materials, or documentation. You may copy, distribute, and display the icons only for the permitted use unless granted explicit permission by Microsoft. Microsoft reserves all other rights.",
        "see https://docs.microsoft.com/en-us/azure/architecture/icons/"
    )

    fun fetch(monoChrome: Boolean) {
        val className = if (monoChrome) "AzureMonoIcons" else "AzureIcons"
        val theme = StructurizrTheme(className, "")
        val lines = URL(url).readText().lines()
        val file = "src/main/java/io/cloudflight/architectureicons/azure/$className.java".asEmptyFile()
        val base64 = "src/main/resources/base64/$className.properties".asFileIfNotExists()
        base64?.let { f ->
            LICENSE_TEXT.forEach {
                f.appendLine("# " + it)
            }
        }
        file.appendLine("package io.cloudflight.architectureicons.azure;")
        file.appendLine()
        file.appendLine("import io.cloudflight.architectureicons.Icon;")
        file.appendLine("import io.cloudflight.architectureicons.PlantUmlSprite;")
        file.appendLine()
        file.appendLine("/**")
        file.appendLine(" * This file is generated from <a href=\"$sourceUrl\">$sourceUrl</a>")
        LICENSE_TEXT.forEach {
            file.appendLine(" * " + it)
        }

        file.appendLine(" */")
        file.appendLine("public class $className {")
        file.appendLine("    private $className() {}")
        file.appendLine("    public static final String STRUCTURIZR_THEME_URL = \"https://raw.githubusercontent.com/cloudflightio/architecture-icons/\" + $className.class.getPackage().getImplementationVersion() + \"/structurizr-themes/$className.json\";")
        file.appendLine("    private static final String ROOT = \"$rootUrl\";")
        file.appendLine("    private static final String COMMONS = ROOT + \"/AzureCommon.puml\";")

        var first = true
        var category: String? = null
        lines.subList(lines.indexOfFirst { it.startsWith("**") }, lines.size).iterator().forEach { line ->
            try {
                if (line.trim().isNotBlank()) {
                    if (line.startsWith("**")) {
                        if (!first) {
                            file.appendLine("    }")
                        } else {
                            first = false
                        }
                        category = line.split("|")[0].trim().removeSurrounding("**")
                        file.appendLine("    public static class $category {")
                        file.appendLine("        private $category() {}")
                    } else {
                        val split = line.split('|')
                        val name = split[1].substringBefore('<').trim()
                        val pumlUrl = split[4].trim()
                        val pngUrl = (if (monoChrome) pumlUrl.replace(
                            ".puml",
                            "%28m%29.png"
                        ) else pumlUrl.replace(".puml", ".png"))

                        val constName = name.toConstName()
                        val iconId = "$className-$constName"

                        file.appendLine("        /**")
                        file.appendLine("         * <img alt=\"$pngUrl\" src=\"$rootUrl/$pngUrl\">")
                        file.appendLine("         */")
                        if (monoChrome) {
                            file.appendLine("        public static final Icon $constName = new Icon(\"$iconId\", ROOT + \"/$pngUrl\",  AzureIcons.$category.$constName.getPlantUmlSprite());")
                        } else {
                            file.appendLine("        public static final Icon $constName = new Icon(\"$iconId\", ROOT + \"/$pngUrl\",  new PlantUmlSprite(\"$name\", COMMONS, ROOT + \"/$pumlUrl\"));")
                        }
                        theme.elements.add(ThemeElement(tag = iconId, icon = rootUrl + "/" + pngUrl))
                        base64?.appendLine(constName + "=" + URL(rootUrl + "/" + pngUrl).readBytes().toBase64())
                    }
                }

            } catch (ex: Exception) {
                println("Cannot split " + line)
            }
        }
        file.appendLine("    }")
        file.appendLine("}")

        val themeFile = java.io.File("structurizr-themes/$className.json")
        themeFile.parentFile.mkdirs()
        themeFile.outputStream().use {
            Json { prettyPrint = true }.encodeToStream(theme, it)
        }
    }
}

