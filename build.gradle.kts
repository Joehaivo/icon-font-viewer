plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.jetbrains.intellij.platform") version "2.0.1"
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

intellijPlatform {
    pluginConfiguration {
        version = "1.1.2"
        description = """
            展示ttf字体文件内的icon(或者叫symbol)、字符以及unicode码点信息.<br><br>
            Display the icons (or symbols), characters, and Unicode code point information inside a TTF font file.<br>
            <img src="https://github.com/Joehaivo/icon-font-viewer/blob/master/docs/1665853268900.png?raw=true" alt="screenshot" width="400" height="271"><br>
            <a href="https://github.com/Joehaivo/icon-font-viewer">Github Source Code</a><br><br>
            <a href="https://github.com/Joehaivo/icon-font-viewer/blob/master/docs/iconfont.ttf?raw=true">Here is a sample icon font(.ttf) file</a><br>
            <a href="https://github.com/Joehaivo/icon-font-viewer/blob/master/docs/iconfont.ttf?raw=true">这有一个示例icon font(.ttf)文件</a><br>
            usage: open .ttf file to view.<br>
            用法：打开.ttf文件查看.<br><br>
        """.trimIndent()
        changeNotes = """
            1.1.2: feat: 采用kotlin UI DSL 2写法重写界面<br>
            1.0.8: fix: 搜索时文字显示乱码; 兼容更早的版本<br>
        """.trimIndent()
        ideaVersion.sinceBuild.set("201")
        ideaVersion.untilBuild.set("242.*")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.runIde {
    jvmArgs = listOf("-Xmx4096m", "-XX:+UnlockDiagnosticVMOptions")
}

dependencies {
    intellijPlatform {
//        bundledPlugin("org.jetbrains.android")
        instrumentationTools()
//        local("/Users/haivo/Applications/Android Studio.app/Contents")
        local("/Applications/DevEco-Studio.app/Contents")
    }
}
