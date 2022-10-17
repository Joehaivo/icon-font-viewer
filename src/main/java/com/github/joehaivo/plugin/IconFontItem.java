package com.github.joehaivo.plugin;

import javax.swing.*;
import java.awt.*;

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
