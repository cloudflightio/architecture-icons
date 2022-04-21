package io.cloudflight.architectureicons.gradle

import java.net.URL

class PlantUmlIconFontSpritesFetcher {

    fun fetch(url: String, className: String) {
        val lines = URL(url).readText().lines()
        val rootUrl = url.substringBeforeLast('/')
        val sourceUrl = url.replace(
            "https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master",
            "https://github.com/tupadr3/plantuml-icon-font-sprites/blob/master/"
        )
        val file = "src/main/java/io/cloudflight/architectureicons/tupadr3/$className.java".asEmptyFile()
        val base64 = "src/main/resources/base64/$className.properties".asEmptyFile()
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
        file.appendLine("    private static final String ROOT = \"$rootUrl\";")
        lines.subList(6, lines.size).forEach { line ->
            try {
                if (line.trim().isNotBlank()) {
                    val split = line.split('|')
                    val name = split[0].trim()
                    val pumlUrl = split[3].trim()
                    val pngUrl = pumlUrl.replace(".puml", ".png")
                    val constName = name.toConstName()

                    file.appendLine("    /**")
                    file.appendLine("     * <img alt=\"$pngUrl\" src=\"$rootUrl/$pngUrl\">")
                    file.appendLine("     */")
                    file.appendLine("    public static final Icon ${name.toConstName()} = new Icon(\"$className-$constName\", ROOT + \"/$pngUrl\", new PlantUmlSprite(\"$name\", ROOT + \"/$pumlUrl\"));")

                    base64.appendLine(constName + "=" + URL(rootUrl + "/" + pngUrl).readBytes().toBase64())
                }
            } catch (ex: Exception) {
                println("Cannot split " + line)
            }
        }
        file.appendLine("}")
    }
}

