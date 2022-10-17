package com.github.joehaivo.plugin

class IconVo(var codePoint: Char, var postScript: String = "", var searched: Boolean = false, var keyword: String = "") {
    /**
     * @return &#xe683;  &#xffff;
     */
    fun getCodePointText(): String {
        return "&#x${Integer.toHexString(codePoint.code)};"
    }
}