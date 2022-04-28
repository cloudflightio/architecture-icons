package io.cloudflight.architectureicons.gradle

import java.io.File
import java.io.InputStream
import java.util.*

fun File.appendLine(line: String? = null) {
    return appendLine(0, line)
}

fun File.appendLine(indent: Int, line: String? = null) {
    if (line != null) {
        appendText(" ".repeat(indent) + line)
    }
    appendText(System.lineSeparator())
}


fun String.toConstName(): String {
    val uppercase = camelCaseToLowerCaseWithUnderscore(this).uppercase()
    if (Character.isDigit(uppercase[0])) {
        return "SPRITE_" + uppercase
    } else {
        return uppercase
    }
}

fun camelCaseToLowerCaseWithUnderscore(string: String): String {
    val result = StringBuffer()

    var lastUppercase = false
    string.forEach {
        if (it.isUpperCase() && !lastUppercase && result.isNotEmpty()) {
            result.append('_')
        }
        lastUppercase = it.isUpperCase()
        result.append(it)
    }
    return result.toString()
}

fun String.asEmptyFile(): File {
    val file = File(this)
    if (file.exists()) {
        file.delete()
    }
    file.parentFile.mkdirs()
    return file
}

fun String.asFileIfNotExists(): File? {
    val file = File(this)
    if (file.exists()) {
        return null
    }
    file.parentFile.mkdirs()
    return file
}


fun ByteArray.toBase64(): String {
    return Base64.getEncoder().encodeToString(this)
}

fun InputStream.toBase64(): String {
    val result: String
    this.use { inputStream ->
        result = inputStream.readBytes().toBase64()
    }
    return result
}