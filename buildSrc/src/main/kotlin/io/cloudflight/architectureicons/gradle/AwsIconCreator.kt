package io.cloudflight.architectureicons.gradle

import java.io.File
import java.net.URL


class AwsIconCreator {

    fun fetch(className: String, directory: File) {

        val iconClass = IconClass(
            packageName = "io.cloudflight.architectureicons.aws",
            sourceUrl = "https://aws.amazon.com/de/architecture/icons/",
            iconRootUrl = "https://raw.githubusercontent.com/cloudflightio/architecture-icons/master/aws-icons/${directory.name}",
            //iconRootUrl = "https://raw.githubusercontent.com/cloudflightio/architecture-icons/\" + ${className}.class.getPackage().getImplementationVersion() + \"/aws-icons/${directory.name}",
            icons = IconGroup(className)
        )

        directory.listFiles().forEach {
            if (it.isDirectory) {
                val group = IconGroup(it.name.replace("-", "_")).also {
                    iconClass.icons.subGroups.add(it)
                }
                it.listFiles().forEach { icon ->
                    group.icons.add(
                        Icon(
                            name = icon.name.substringBeforeLast("."),
                            pngUrl = it.name + "/" + icon.name,
                            pngFile = icon
                        )
                    )
                }
            } else {
                iconClass.icons.icons.add(Icon(name = it.name.substringBeforeLast("."), pngUrl = it.name, pngFile = it))
            }
        }


        IconClassGenerator().generate(iconClass)
    }
}

