package com.github.joehaivo.plugin;

import com.github.joehaivo.plugin.IconVo;
import com.intellij.openapi.fileEditor.FileEditor;

import javax.swing.*;

public abstract class IconFontViewer implements FileEditor {
    protected JPanel rootPanel;
    protected JLabel jLabelFontName;
    protected JTextField jTextSearch;
    protected JButton jButtonSearch;
    protected JList<IconVo> jListIcons;
    protected JScrollPane jScrollPanel;

    private void createUIComponents() {
        rootPanel = new JPanel();
    }
}
