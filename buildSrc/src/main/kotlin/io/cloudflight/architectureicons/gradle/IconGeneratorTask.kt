package io.cloudflight.architectureicons.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class IconGeneratorTask : DefaultTask() {

    @TaskAction
    fun generateIcons() {
        PlantUmlIconFontSpritesFetcher().apply {
            fetch("https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/font-awesome/index.md", "FontAwesome4")
            fetch("https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/font-awesome-5/index.md", "FontAwesome5")
            fetch("https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/devicons/index.md", "DevIcons")
            fetch("https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/govicons/index.md", "GovIcons")
            fetch("https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/weather/index.md", "Weather")
            fetch("https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/material/index.md", "Material")
            fetch("https://raw.githubusercontent.com/tupadr3/plantuml-icon-font-sprites/master/devicons2/index.md", "DevIcons2")
        }

         AzurePlantUmlIconFetcher().apply {
             fetch(false)
             fetch(true)
         }

        AwsIconCreator().apply {
            fetch("AwsArchitectureIcons", project.file("aws-icons/Architecture-Service"))
            fetch("AwsCategoryIcons", project.file("aws-icons/Category"))
            fetch("AwsResourceIcons", project.file("aws-icons/Resource"))
        }
    }
}