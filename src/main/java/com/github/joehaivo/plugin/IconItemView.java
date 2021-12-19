package com.github.joehaivo.plugin;

import com.github.joehaivo.plugin.IconFontGlyph;
import com.github.joehaivo.plugin.IconVo;

import javax.swing.*;
import java.awt.*;

public class IconItemView extends JComponent {
    private JPanel rootPanel;
    private IconFontGlyph iconFontGlyph;
    private JLabel jTextCodePoint;
    private JLabel jTextScript;

    private void createUIComponents() {
        rootPanel = new JPanel();
    }

    public IconItemView(IconFontGlyph iconFontGlyph) {
        this.iconFontGlyph = iconFontGlyph;
        setUI();
    }

    private void setUI() {
        IconVo iconVo = iconFontGlyph.getIconVo();
        jTextCodePoint.setText("&#x" + Integer.toHexString(iconVo.getCodePoint()) + ";");
        jTextScript.setText(iconVo.getPostScript());
    }

    @Override
    public Component getComponent(int n) {
        return rootPanel;
    }
}
