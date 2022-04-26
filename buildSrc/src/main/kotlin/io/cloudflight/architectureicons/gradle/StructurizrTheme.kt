package io.cloudflight.architectureicons.gradle

import kotlinx.serialization.Serializable

@Serializable
data class StructurizrTheme(val name: String, val description: String, val elements: MutableList<ThemeElement> = mutableListOf()) {
}

@Serializable
data class ThemeElement(val tag: String, val icon: String)