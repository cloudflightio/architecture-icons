package io.cloudflight.architectureicons.gradle

import java.net.URL

class PlantUmlIconFontSpritesFetcher {

    fun fetch(url: String, className: String) {
        val generator = IconClassGenerator()

        val lines = URL(url).readText().lines()
        val rootUrl = url.substringBeforeLast('/')
        val sourceUrl = url.replace(
            "https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master",
            "https://github.com/tupadr3/plantuml-icon-font-sprites/blob/master/"
        )
        val iconClass = IconClass(
            packageName = "io.cloudflight.architectureicons.tupadr3",
            sourceUrl = sourceUrl,
            iconRootUrl = rootUrl,
            icons = IconGroup(className)
        )

        lines.subList(6, lines.size).forEach { line ->
            try {
                if (line.trim().isNotBlank()) {
                    val split = line.split('|')
                    val name = split[0].trim()
                    val pumlUrl = split[3].trim()
                    val pngUrl = pumlUrl.replace(".puml", ".png")

                    iconClass.icons.icons.add(Icon(name = name, pngUrl = pngUrl, pumlUrl = pumlUrl))
                }
            } catch (ex: Exception) {
                throw RuntimeException("Cannot split " + line, ex)
            }
        }

        generator.generate(iconClass)
    }
}

