// 这里只对插件和注解处理器进行配置，库的依赖在 module.yaml 中进行配置
plugins {
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        // 编译后，生成的数据库文件会放在 build/generated/sqldelight/code/ 下会生产 AppDatabase.kt 文件 和 .sq 对应的 kotlin 文件
        // 如果没有生成，说明配置有问题
        create("SqlDelightDatabase") {
            // 由于 Amper 的目录结构和传统的 kotlin 项目不同，这里需要重新指定 srcDirs 的路径
            // 我们就可以直接在 src 目录下新建一个 sqldelight 目录，然后将所有 sq 文件放到这个目录下
            // 默认的识别路径是 src/main/sqldelight
            srcDirs.setFrom("src/sqldelight")
            packageName.set("app.data.sqldelight")
        }
    }
}