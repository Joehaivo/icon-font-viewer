plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    id("org.jetbrains.intellij") version "1.9.0"
}

repositories {
    mavenCentral()
}

// https://github.com/JetBrains/gradle-intellij-plugin
intellij {
//    version.set("2021.2") // 设置debug时的idea版本
//    type.set("IC") // Target IDE Platform
    localPath.set("/Applications/Android Studio.app/Contents")
    plugins.set(listOf("java"))
}

dependencies {
//    implementation("org.apache.pdfbox:fontbox:2.0.27")
}

group = "com.github.Joehaivo"
version = "1.0.8"

tasks {
//    instrumentCode {
//        compilerVersion.set("211.7628.21")
//    }
    patchPluginXml {
        pluginDescription.set("""
            A plugin for displaying the svg icons and unicode of trueType (.ttf) file that usually containing icon glyphs.<br><br>
            一个用来展示trueType(.ttf)文件中的SVG图标和unicode码点的插件<br>
            <a href="https://github.com/Joehaivo/icon-font-viewer">Github Source Code</a><br><br>
            usage: open .ttf file to view.<br>
            用法：打开.ttf文件查看.<br><br>
        """.trimIndent())
//        changeNotes.set("""
//        Add IconFontViewer plugin.<br>
//        """.trimIndent())
        sinceBuild.set("201")
        untilBuild.set("223")
    }
    buildSearchableOptions {
        enabled = false
    }
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

//    jar {
        // 将runtimeClasspath里的jar包(dependencies下的依赖)都打进jar中，打成Fat-jar
//        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
