package io.cloudflight.architectureicons.gradle

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

class IconClassGenerator {

    fun generate(iconClass: IconClass) {
        val theme = StructurizrTheme(iconClass.className, "")
        val file = "src/main/java/${iconClass.packageName.replace(".", "/")}/${iconClass.className}.java".asEmptyFile()
        val base64 = "src/main/resources/base64/${iconClass.className}.properties".asFileIfNotExists()
        base64?.let { f ->
            iconClass.licenseText.forEach {
                f.appendLine("# " + it)
            }
        }
        file.appendLine("package ${iconClass.packageName};")
        file.appendLine()
        file.appendLine("import io.cloudflight.architectureicons.Icon;")
        file.appendLine("import io.cloudflight.architectureicons.PlantUmlSprite;")
        file.appendLine()
        file.appendLine("/**")
        file.appendLine(" * This file is generated from <a href=\"${iconClass.sourceUrl}\">${iconClass.sourceUrl}</a>")
        iconClass.licenseText.forEach {
            file.appendLine(" * " + it)
        }

        file.appendLine(" */")
        file.appendLine("public class ${iconClass.className} {")
        file.appendLine("    private ${iconClass.className}() {}")
        file.appendLine("    /**")
        file.appendLine("     * This constants returns the URL to a <a href=\"https://structurizr.com/help/themes\">Structurizr Theme</a> containing all icons of this class. Use {@link Icon#getName()} as tag name in your structurizr models.")
        file.appendLine("     */")
        file.appendLine("    public static final String STRUCTURIZR_THEME_URL = \"https://raw.githubusercontent.com/cloudflightio/architecture-icons/\" + ${iconClass.className}.class.getPackage().getImplementationVersion() + \"/structurizr-themes/${iconClass.className}.json\";")
        file.appendLine("    private static final String ROOT = \"${iconClass.iconRootUrl}\";")
        iconClass.pumlIncludes.forEach {
            file.appendLine("    private static final String ${it.first} = ROOT + \"${it.second}\";")
        }

        val iconGroup = iconClass.icons
        val counter = AtomicInteger(0)
        addIcons(iconGroup, iconClass, indent = 4, file, base64, theme, counter)
        iconGroup.subGroups.forEach {
            file.appendLine(4, "public static class ${it.className} {")
            file.appendLine(8, "private ${it.className}() {}")
            addIcons(it, iconClass, indent = 8, file, base64, theme, counter)
            file.appendLine(4, "}")
        }

        file.appendLine("}")

        val themeFile = java.io.File("structurizr-themes/${iconClass.className}.json")
        themeFile.parentFile.mkdirs()
        themeFile.outputStream().use {
            Json { prettyPrint = true }.encodeToStream(theme, it)
        }
    }

    private fun addIcons(
        iconGroup: IconGroup,
        iconClass: IconClass,
        indent: Int,
        javaFile: File,
        base64File: File?,
        theme: StructurizrTheme,
        counter: AtomicInteger
    ) {
        iconGroup.icons.forEach { icon ->

            val constName = icon.name.toConstName()
            val iconId = "${iconClass.className}-${counter.incrementAndGet()}"
            var iconName = icon.name
            if (iconGroup != iconClass.icons) {
                iconName += " (" + iconGroup.className + ")"
            }

            javaFile.appendLine(indent, "/**")
            javaFile.appendLine(
                indent,
                " * <img alt=\"${icon.name}\" src=\"${iconClass.iconRootUrl}/${icon.pngUrl}\">"
            )
            javaFile.appendLine(indent, " */")
            if (icon.pumlUrl == null && icon.pumlReference == null) {
                javaFile.appendLine(
                    indent,
                    "public static final Icon $constName = new Icon(\"$iconId\", \"${iconName}\", ROOT + \"/${icon.pngUrl}\",  null);"
                )
            } else if (icon.pumlUrl != null) {
                javaFile.appendLine(
                    indent,
                    "public static final Icon $constName = new Icon(\"$iconId\", \"${iconName}\", ROOT + \"/${icon.pngUrl}\", new PlantUmlSprite(\"${icon.name}\", ${
                        iconClass.pumlIncludes.map { it.first }.joinToString { it + ", " }
                    }ROOT + \"/${icon.pumlUrl}\"));"
                )
            } else if (icon.pumlReference != null) {
                javaFile.appendLine(
                    indent,
                    "public static final Icon $constName = new Icon(\"$iconId\", \"${iconName}\", ROOT + \"/${icon.pngUrl}\", ${icon.pumlReference});"
                )
            }
                theme.elements.add(ThemeElement(tag = iconName, icon = iconClass.iconRootUrl + "/" + icon.pngUrl))

            if (icon.pngFile != null) {
                base64File?.appendLine(
                    counter.get().toString() + "=" + icon.pngFile.readBytes().toBase64()
                )
            } else {
                base64File?.appendLine(
                    counter.get().toString() + "=" + URL(iconClass.iconRootUrl + "/" + icon.pngUrl).readBytes()
                        .toBase64()
                )
            }
        }
    }
}

