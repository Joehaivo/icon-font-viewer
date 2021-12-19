package com.github.joehaivo.plugin

import com.github.joehaivo.util.Notifier
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import org.apache.fontbox.ttf.TTFParser
import org.apache.fontbox.ttf.TrueTypeFont
import java.awt.Font
import java.awt.FontFormatException
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.beans.PropertyChangeListener
import java.io.IOException
import java.util.*
import javax.swing.DefaultListModel
import javax.swing.JComponent
import javax.swing.JList

class IconFontViewerKt(var project: Project, var ttfFile: VirtualFile) : IconFontViewer() {
    private val myUserDataHolder = UserDataHolderBase()
    private var font: Font? = null
    private var ttfParser: TrueTypeFont? = null
    private var iconVos = mutableListOf<IconVo>()
    private var iconsMap = mutableMapOf<String, Char>()
    init {
        setUI()
    }

    override fun getComponent(): JComponent {
        return rootPanel
    }

    private fun setUI() {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, ttfFile.inputStream).deriveFont(30f)
            if (font == null) {
                jLabelFontName.text = "Unable to parse the file."
                return
            }
            jLabelFontName.text = font!!.fontName
            val listModel = DefaultListModel<IconVo>()
            jListIcons.layoutOrientation = JList.HORIZONTAL_WRAP
            jListIcons.visibleRowCount = -1
            jListIcons.model = listModel
            jListIcons.cellRenderer = IconItemRender(font!!)
            jScrollPanel.viewport.view = jListIcons

            parseTtf()
            if (iconVos.isNotEmpty()) {
                for (vo in iconVos) {
                    listModel.addElement(vo)
                }
            }
            jTextSearch.addKeyListener(object : KeyListener {
                override fun keyTyped(e: KeyEvent?) {
                }

                override fun keyPressed(e: KeyEvent?) {
                    if (e?.keyCode == KeyEvent.VK_ENTER) {
                        doSearch(iconVos, listModel)
                    }
                }

                override fun keyReleased(e: KeyEvent?) {
                }
            })
            jButtonSearch.addActionListener { e: ActionEvent ->
                doSearch(iconVos, listModel)
            }
        } catch (e: FontFormatException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
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
        iconVos: List<IconVo>,
        listModel: DefaultListModel<IconVo>
    ) {
        if (font == null || jTextSearch.text.isEmpty()) {
            return
        }
        val keyword = jTextSearch.text.lowercase(Locale.getDefault())
        val foundIcons = iconVos.filter {
            it.postScript.lowercase(Locale.getDefault()).contains(keyword) || String.format("%04x", it.codePoint.code)
                .contains(keyword)
        }
        if (foundIcons.isEmpty()) {
            Notifier.notifyInfo(project, "${jTextSearch.text} not found.")
            return
        }
        listModel.clear()
        iconVos.forEach {
            it.searched = false
            listModel.addElement(it)
        }
        foundIcons.forEach {
            it.searched = true
            listModel.set(iconVos.indexOf(it), it)
        }
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return jListIcons
    }

    override fun getName(): String {
        return "IconFont Viewer"
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
}