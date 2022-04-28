package io.cloudflight.architectureicons.gradle

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

        val iconClass = IconClass(
            packageName = "io.cloudflight.architectureicons.azure",
            sourceUrl = sourceUrl,
            iconRootUrl = rootUrl,
            licenseText = LICENSE_TEXT,
            icons = IconGroup(className),
            pumlIncludes = listOf(Pair("COMMONS", "/AzureCommon.puml"))
        )

        val lines = URL(url).readText().lines()

        var category: IconGroup? = null
        lines.subList(lines.indexOfFirst { it.startsWith("**") }, lines.size).iterator().forEach { line ->
            try {
                if (line.trim().isNotBlank()) {
                    if (line.startsWith("**")) {
                        category = IconGroup(className = line.split("|")[0].trim().removeSurrounding("**")).also {
                            iconClass.icons.subGroups.add(it)
                        }
                    } else {
                        val currentCategory = category ?: throw RuntimeException("no category available")
                        val split = line.split('|')
                        val name = split[1].substringBefore('<').trim()
                        val pumlUrl = split[4].trim()
                        val pngUrl = (if (monoChrome) pumlUrl.replace(
                            ".puml",
                            "%28m%29.png"
                        ) else pumlUrl.replace(".puml", ".png"))

                        val constName = name.toConstName()

                        if (monoChrome) {
                            currentCategory.icons.add(
                                Icon(
                                    name = name,
                                    pngUrl = pngUrl,
                                    pumlReference = "AzureIcons.${currentCategory.className}.$constName.getPlantUmlSprite()"
                                )
                            )
                        } else {
                            currentCategory.icons.add(Icon(name = name, pngUrl = pngUrl, pumlUrl = pumlUrl))
                        }
                    }
                }
            } catch (ex: Exception) {
                throw RuntimeException("cannot split " + line, ex)
            }
        }
        IconClassGenerator().generate(iconClass)
    }
}

