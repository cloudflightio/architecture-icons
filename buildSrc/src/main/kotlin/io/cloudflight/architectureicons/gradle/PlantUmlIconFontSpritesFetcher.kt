package io.cloudflight.architectureicons.gradle

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.net.URL

class PlantUmlIconFontSpritesFetcher {

    fun fetch(url: String, className: String) {
        val theme = StructurizrTheme(className, "")

        val lines = URL(url).readText().lines()
        val rootUrl = url.substringBeforeLast('/')
        val sourceUrl = url.replace(
            "https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master",
            "https://github.com/tupadr3/plantuml-icon-font-sprites/blob/master/"
        )
        val file = "src/main/java/io/cloudflight/architectureicons/tupadr3/$className.java".asEmptyFile()
        val base64 = "src/main/resources/base64/$className.properties".asFileIfNotExists()
        file.appendLine("package io.cloudflight.architectureicons.tupadr3;")
        file.appendLine()
        file.appendLine("import io.cloudflight.architectureicons.Icon;")
        file.appendLine("import io.cloudflight.architectureicons.PlantUmlSprite;")
        file.appendLine()
        file.appendLine("/**")
        file.appendLine(" * This file is generated from <a href=\"$sourceUrl\">$sourceUrl</a>")
        file.appendLine(" */")
        file.appendLine("public class $className {")
        file.appendLine("    private $className() {}")
        file.appendLine("    private static final String STRUCTURIZR_THEME_URL = \"https://raw.githubusercontent.com/cloudflightio/architecture-icons/\" + $className.class.getPackage().getImplementationVersion() + \"/structurizr-themes/$className.json\";")
        file.appendLine("    private static final String ROOT = \"$rootUrl\";")
        lines.subList(6, lines.size).forEach { line ->
            try {
                if (line.trim().isNotBlank()) {
                    val split = line.split('|')
                    val name = split[0].trim()
                    val pumlUrl = split[3].trim()
                    val pngUrl = pumlUrl.replace(".puml", ".png")
                    val constName = name.toConstName()
                    val iconId = "$className-$constName"

                    file.appendLine("    /**")
                    file.appendLine("     * <img alt=\"$pngUrl\" src=\"$rootUrl/$pngUrl\">")
                    file.appendLine("     */")
                    file.appendLine("    public static final Icon ${name.toConstName()} = new Icon(\"$iconId\", ROOT + \"/$pngUrl\", new PlantUmlSprite(\"$name\", ROOT + \"/$pumlUrl\"));")

                    theme.elements.add(ThemeElement(tag = iconId, icon = rootUrl + "/" + pngUrl))
                    base64?.appendLine(constName + "=" + URL(rootUrl + "/" + pngUrl).readBytes().toBase64())
                }
            } catch (ex: Exception) {
                println("Cannot split " + line)
            }
        }
        file.appendLine("}")

        val themeFile = java.io.File("structurizr-themes/$className.json")
        themeFile.parentFile.mkdirs()
        themeFile.outputStream().use {
            Json { prettyPrint = true }.encodeToStream(theme, it)
        }
    }
}

