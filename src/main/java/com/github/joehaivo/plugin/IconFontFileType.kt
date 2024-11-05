package com.github.joehaivo.plugin

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class IconFontFileType : FileType {
    companion object {
        val INSTANCE = IconFontFileType()
    }

    override fun getName(): String {
        return "IconFont"
    }

    override fun getDescription(): String {
        return "IconFont file"
    }

    override fun getDefaultExtension(): String {
        return "ttf"
    }

    override fun getIcon(): Icon {
        return IconFontIcon.FILE
    }

    override fun isBinary(): Boolean {
        return true
    }

    override fun isReadOnly(): Boolean {
        return false
    }

    override fun getCharset(file: VirtualFile, content: ByteArray): @NonNls String? {
        return null
    }
}

class IconFontIcon {
    companion object {
        val FILE = IconLoader.getIcon("/icons/icon.svg", IconFontIcon::class.java)
    }
}