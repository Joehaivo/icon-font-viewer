package com.github.joehaivo.plugin

class IconFontItemImpl(iconFontGlyph: IconFontGlyph) : IconFontItem(iconFontGlyph) {
    init {
        setUI()
    }

    private fun setUI() {
        val iconVo = iconFontGlyph.iconVo
        val fullCodePoint = "&#x" + Integer.toHexString(iconVo.codePoint.code) + ";"

        jTextCodePoint.text = getHighlightText(iconVo.keyword, fullCodePoint)
        jTextScript.text = getHighlightText(iconVo.keyword, iconVo.postScript)
    }

    private fun getHighlightText(keyword: String, targetText: String): String {
        if (keyword.isBlank() || targetText.isBlank()) {
            return targetText
        }
        val keywordRange = Regex(".*($keyword).*").find(targetText)?.groups?.get(1)?.range
        return if (keywordRange != null) {
            "<html>${targetText.substring(0, keywordRange.first).intern()}"
                .plus("<font color='red'>${targetText.substring(keywordRange).intern()}</font>")
                .plus("${targetText.substring(keywordRange.last, targetText.lastIndex).intern()}</html>")
        } else {
            targetText
        }
    }
}