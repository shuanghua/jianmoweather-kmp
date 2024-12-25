// 这里只对插件和注解处理器进行配置，其它库的依赖在 module.yaml 中进行配置
plugins {
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.ksp)
}

dependencies {
//    add("kspAndroid", libs.androidx.room.compiler)
//    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
//    add("kspIosX64", libs.androidx.room.compiler)
//    add("kspIosArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}