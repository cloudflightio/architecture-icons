package io.cloudflight.architectureicons.gradle

import java.io.File

data class IconClass(
    val packageName: String,
    val sourceUrl: String,
    val licenseText: List<String> = emptyList(),
    val iconRootUrl: String,
    val icons: IconGroup,
    val pumlIncludes: List<Pair<String, String>> = emptyList()
) {
    val className = icons.className

}

data class IconGroup(
    val className: String,
    val icons: MutableList<Icon> = mutableListOf(),
    var subGroups: MutableList<IconGroup> = mutableListOf()
) {
}

data class Icon(
    val name: String,
    val pngUrl: String,
    val pumlUrl: String? = null,
    val pumlReference: String? = null,
    val pngFile: File? = null
)