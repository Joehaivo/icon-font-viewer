package com.github.joehaivo.plugin

class IconFontItemImpl(iconFontGlyph: IconFontGlyph) : IconFontItem(iconFontGlyph) {
    init {
        jTextCodePoint.isFocusable = true
        setUI()
    }

    private fun setUI() {
        val iconVo = iconFontGlyph.iconVo
        jTextCodePoint.text = getHighlightText(iconVo.keyword, iconVo.getCodePointText())
        jTextScript.text = getHighlightText(iconVo.keyword, iconVo.postScript)
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
            val poundKeyIndex = text.indexOf("#")
            if (poundKeyIndex >= 0 && poundKeyIndex + 1 < text.length) {
                text.insert(poundKeyIndex + 1, " ")
            }
            return text.toString()
        } else {
            targetText
        }
    }
}