package com.github.joehaivo.plugin

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import org.apache.fontbox.ttf.TTFParser
import org.apache.fontbox.ttf.TrueTypeFont
import java.awt.Font
import java.beans.PropertyChangeListener
import java.util.*
import javax.swing.DefaultListModel
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class IconFontViewer(var project: Project, var ttfFile: VirtualFile) : FileEditor {
    private val myUserDataHolder = UserDataHolderBase()
    private var font: Font? = null
    private var ttfParser: TrueTypeFont? = null
    private var iconVos = mutableListOf<IconVo>()
    private var iconsMap = mutableMapOf<String, Char>()
    private val model = Model()
    val listModel = DefaultListModel<IconVo>()

    init {
        setUI()
    }

    override fun getComponent(): JComponent {
        val pan = panel {
            indent {
                row {
                    text("FontName : ").gap(RightGap.SMALL)
                    text("").bindText(model::fontName).gap(RightGap.COLUMNS)

                    text("Search : ").gap(RightGap.SMALL)
                    textField().also { tf ->
                        setInputChangedListener(tf)
                    }.gap(RightGap.SMALL)
                    text("").bindText(model::searchResultTip)
                }
            }

            row {
                scrollCell(fontJBList()).align(Align.FILL)
            }.resizableRow()
        }
        return pan
    }

    private fun setInputChangedListener(tf: Cell<JBTextField>) {
        tf.component.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) {
                valueChanged()
            }

            override fun removeUpdate(e: DocumentEvent?) {
                valueChanged()
            }

            override fun changedUpdate(e: DocumentEvent?) {
                valueChanged()
            }

            fun valueChanged() {
                runWriteAction {
                    val keyword = tf.component.text.trim().lowercase(Locale.getDefault())
                    iconVos.forEach { it.keyword = "" }
                    if (keyword.isBlank()) {
                        listModel.clear()
                        listModel.addAll(iconVos)
                        model.searchResultTip = ""
                    } else {
                        doSearch(keyword, listModel)
                    }
                }
            }
        })
    }

    private fun setUI() {
        runReadAction {
            // 此处更改icon大小
            font = Font.createFont(Font.TRUETYPE_FONT, ttfFile.inputStream).deriveFont(40f)
            if (font == null) {
                model.fontName = "Unable to parse the file."
                return@runReadAction
            }
            model.fontName = font!!.fontName

            parseTtf()
            if (iconVos.isNotEmpty()) {
                listModel.addAll(iconVos)
            }
        }
    }

    private fun fontJBList(): JBList<IconVo> {
        val jListIcons: JBList<IconVo> = JBList<IconVo>(iconVos)
        jListIcons.layoutOrientation = JList.HORIZONTAL_WRAP
        jListIcons.visibleRowCount = -1
        jListIcons.model = listModel
        jListIcons.fixedCellWidth = 140
        jListIcons.fixedCellHeight = 165
        jListIcons.cellRenderer = IconItemRender(font!!)
        jListIcons.isFocusable = true
        return jListIcons
    }

    private fun parseTtf() {
        ttfParser = TTFParser().parse(ttfFile.inputStream) ?: return
        val cmapLookup = ttfParser!!.getUnicodeCmapLookup(true)
        val glyphNames = ttfParser!!.postScript.glyphNames
        val glyphIdToCharacterCodeField = cmapLookup.javaClass.getDeclaredField("glyphIdToCharacterCode")
        glyphIdToCharacterCodeField.isAccessible = true
        val glyphIdToCharacterCode = glyphIdToCharacterCodeField.get(cmapLookup) as IntArray
        if (glyphIdToCharacterCode.isNotEmpty()) {
            glyphIdToCharacterCode.forEachIndexed { index, i ->
                if (glyphIdToCharacterCode.size == glyphNames?.size) {
                    val iconVo = IconVo(i.toChar(), glyphNames[index])
                    iconVos.add(iconVo)
                    iconsMap[glyphNames[index]] = i.toChar()
                } else {
                    val iconVo = IconVo(i.toChar(), "")
                    iconVos.add(iconVo)
                }
            }
        }
    }

    private fun doSearch(
        keyword: String,
        listModel: DefaultListModel<IconVo>
    ) {
        if (font == null) {
            model.searchResultTip = "Unable to parse the file. search abort! "
            return
        }
        val foundIcons = iconVos.filter {
            it.postScript.lowercase().contains(keyword)
                    || it.getCodePointTextHex().contains(keyword)
                    || it.getCodePointTextInt().contains(keyword)
        }
        listModel.clear()
        foundIcons.forEach { it.keyword = keyword }
        listModel.addAll(foundIcons)
        model.searchResultTip = "${foundIcons.size} item found."
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return null
    }

    override fun getName(): String {
        return "Icon-Font-Viewer"
    }


    override fun getState(level: FileEditorStateLevel): FileEditorState {
        return FileEditorState.INSTANCE
    }

    override fun setState(state: FileEditorState) {}

    override fun isModified(): Boolean {
        return false
    }

    override fun isValid(): Boolean {
        return ttfFile.isValid
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}

    override fun getCurrentLocation(): FileEditorLocation? {
        return null
    }

    override fun dispose() {
        ttfParser?.close()
        font = null
    }

    override fun <T> getUserData(key: Key<T>): T? {
        return myUserDataHolder.getUserData(key)
    }

    override fun <T> putUserData(key: Key<T>, value: T?) {
        myUserDataHolder.putUserData(key, value)
    }

    override fun getFile(): VirtualFile {
        return ttfFile
    }
}

internal data class Model(
    var fontName: String = "unknown",
    var searchResultTip: String = ""
)