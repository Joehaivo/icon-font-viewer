package com.github.joehaivo.plugin

import com.intellij.util.ui.UIUtil
import java.awt.*
import java.awt.font.FontRenderContext
import java.awt.font.GlyphVector
import java.awt.geom.Rectangle2D
import javax.swing.Icon
import javax.swing.JComponent


class IconFontGlyph(
    var trueTypeFont: Font,
    var iconVo: IconVo,
    var fontColor: Color = if (iconVo.searched) UIUtil.getErrorForeground() else UIUtil.getTextFieldForeground()
) : JComponent(), Icon {
    private var glyphVector: GlyphVector? = null
    private var rectangle2D: Rectangle2D? = null

    init {
        val context = FontRenderContext(null, true, true)
        glyphVector = trueTypeFont.createGlyphVector(context, iconVo.codePoint.toString())
        rectangle2D = glyphVector?.getGlyphMetrics(0)?.bounds2D
        repaint()
    }

    // ---------------------icon---------------------
    override fun paintIcon(c: Component?, graphics: Graphics?, x: Int, y: Int) {
        rectangle2D ?: return
        glyphVector ?: return
        val g = graphics as Graphics2D
        g.color = fontColor
        g.drawGlyphVector(glyphVector, x.toFloat(), (y + rectangle2D!!.height).toFloat())
    }

    override fun getIconWidth(): Int {
        return rectangle2D?.bounds?.width ?: 0
    }

    override fun getIconHeight(): Int {
        return rectangle2D?.bounds?.height ?: 0
    }

    // --------------------JComponent---------------
    override fun paintComponent(g1: Graphics?) {
        rectangle2D ?: return
        glyphVector ?: return
        val g = g1 as Graphics2D
        g.color = fontColor
        g.drawGlyphVector(glyphVector, (-rectangle2D!!.x).toFloat(), (-rectangle2D!!.y).toFloat())
    }

    override fun getMinimumSize(): Dimension {
        return rectangle2D?.bounds?.size ?: Dimension()
    }

    override fun getPreferredSize(): Dimension {
        return rectangle2D?.bounds?.size ?: Dimension()
    }
}