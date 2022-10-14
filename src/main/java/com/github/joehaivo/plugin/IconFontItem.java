package com.github.joehaivo.plugin;

import com.intellij.openapi.vcs.changes.ignore.util.RegexUtil;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IconFontItem extends JComponent {
    protected JPanel rootPanel;
    protected IconFontGlyph iconFontGlyph;
    protected JLabel jTextCodePoint;
    protected JLabel jTextScript;

    private void createUIComponents() {
        rootPanel = new JPanel();
    }

    public IconFontItem(IconFontGlyph iconFontGlyph) {
        this.iconFontGlyph = iconFontGlyph;
    }

    @Override
    public Component getComponent(int n) {
        return rootPanel;
    }
}
