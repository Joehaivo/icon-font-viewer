package com.github.joehaivo.plugin

class IconVo(var codePoint: Char, var postScript: String = "", var searched: Boolean = false, var keyword: String = "") {
    /**
     * @return 0xe683;  0xffff;
     */
    fun getCodePointTextHex(): String {
        return "0x${Integer.toHexString(codePoint.code)}"
    }

    // 64578
    fun getCodePointTextInt(): String {
        return codePoint.code.toString(10)
    }
}