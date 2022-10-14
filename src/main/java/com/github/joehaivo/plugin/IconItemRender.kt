package com.github.joehaivo.plugin

import java.awt.Component
import java.awt.Font
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.ListCellRenderer

class IconItemRender(var font1: Font): JComponent(), ListCellRenderer<IconVo> {
    override fun getListCellRendererComponent(
        list: JList<out IconVo>?,
        value: IconVo,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        return IconFontItemImpl(IconFontGlyph(font1, value)).getComponent(0)
    }
}