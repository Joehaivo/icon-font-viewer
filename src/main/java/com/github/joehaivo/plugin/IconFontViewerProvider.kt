package com.github.joehaivo.plugin

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import org.jdom.Element

class IconFontViewerProvider: FileEditorProvider, DumbAware {
    override fun accept(project: Project, file: VirtualFile): Boolean {
        return FileTypeRegistry.getInstance().isFileOfType(file, IconFontFileType.INSTANCE)
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return IconFontViewerImpl(project, file)
    }


    override fun disposeEditor(editor: FileEditor) {
        Disposer.dispose(editor)
    }

    override fun readState(
        sourceElement: Element, project: Project,
        file: VirtualFile
    ): FileEditorState {
        return FileEditorState.INSTANCE
    }

    override fun writeState(state: FileEditorState, project: Project, targetElement: Element) {}

    override fun getEditorTypeId(): String {
        return "iconfonteviewer"
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR
    }
}