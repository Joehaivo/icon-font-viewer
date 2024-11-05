package com.github.joehaivo.plugin

import com.intellij.ui.Colors
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import java.awt.Component
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.ListCellRenderer

class IconItemRender(var myFont: Font) : JComponent(), ListCellRenderer<IconVo> {
    override fun getListCellRendererComponent(
        list: JList<out IconVo>?,
        iconVo: IconVo,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        return panel {
            row {
                cell(IconFontGlyph(myFont, iconVo)).align(Align.CENTER)
            }.topGap(TopGap.MEDIUM)
            row {
                text(getHighlightText(iconVo.keyword, iconVo.getCodePointTextHex())).align(Align.CENTER)
            }
            row {
                text(getHighlightText(iconVo.keyword, iconVo.getCodePointTextInt())).align(Align.CENTER)
            }
            row {
                text(getHighlightText(iconVo.keyword, iconVo.postScript).ifBlank { "-" }).align(Align.CENTER)
            }
        }.withBorder(BorderFactory.createLineBorder(Colors.DISABLED_COLOR, 1, false))
    }

    /**
     * @return <html>&# x<font color='red'>e683</font>;</html>
     * @return <html>she<font color='red'>zhi</font>_line;</html>
     */
    private fun getHighlightText(keyword: String, targetText: String): String {
        if (keyword.isBlank() || targetText.isBlank()) {
            return targetText
        }
        val keywordRange = Regex(".*($keyword).*").find(targetText.lowercase())?.groups?.get(1)?.range
        return if (keywordRange != null) {
            val text = StringBuilder("<html>")
                .append(targetText.substring(0, keywordRange.first))
                .append("<font color='red'>${targetText.substring(keywordRange)}</font>")

            if (keywordRange.last + 1 <= targetText.lastIndex) {
                text.append(targetText.substring(keywordRange.last + 1, targetText.length))
            }
            text.append("</html>")
            return text.toString()
        } else {
            targetText
        }
    }
}