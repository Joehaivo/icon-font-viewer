package com.github.joehaivo.plugin;

import com.intellij.largeFilesEditor.editor.LargeFileEditor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public abstract class IconFontViewer implements FileEditor {
    protected JPanel rootPanel;
    protected JLabel jLabelFontName;
    protected JTextField jTextInput;
    protected JList<IconVo> jListIcons;
    protected JScrollPane jScrollPanel;
    protected JLabel jLabelSearchResult;

    private void createUIComponents() {
        rootPanel = new JPanel();
    }
}
