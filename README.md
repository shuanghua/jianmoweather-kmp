### 简墨天气 的 kmp 项目
> 目前只完成 Android 端，因为还有些库还没有适配 kmp，像权限库，定位库等还没迁移，先完善下层的公共代码，之后再计划实现 iOS 端的特定平台代码

采用 [Amper](https://github.com/JetBrains/amper) + gradle kmp 的项目目录结构, Amper 是 JetBrains 开发的新一代构建工具， 可以单独构建，也可以基于 gradle 进行构建。
由于 Amper 还处于开发阶段，所以本项目还是使用 gradle 构建， 但目录结构和 Amper 项目结构保持一致，方便后续迁移至 Amper 构建工具。

- 数据库: [SQLDelight](https://github.com/sqldelight/sqldelight)
> 简墨天气 的 Android 版用的是 room, room 最新版本也是支持 kmp 的, 但由于 room 使用了 ksp 插件, 在特定的 kotlin 版本上可能无法正常工作，再加上使用 Amper, 无法降级 kotlin 版本, 所以被迫使用 SqlDelight 替代。

- 网络库: [Ktor](https://github.com/ktorio/ktor)

- 日志库: [Napier](https://github.com/AAkira/Napier)

- UI 库: [Jetbrains Compose](https://github.com/JetBrains/compose-multiplatform)
> 和 Jetpack compose 也就依赖库的名字不一样，里面引入的包名也是一样的 androidx.compose.*, ui 界面的迁移是最轻松的一部分了

- 导航库: [Jetbrains navigation-compose](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html#type-safe-navigation)
- ViewModel: [Jetbrains viewmodel-compose](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-viewmodel.html)

- 依赖注入库: [Koin](https://github.com/InsertKoinIO/koin)
 
- 图片加载库: [Coil](https://github.com/coil-kt/coil)

- datastore: [google datastore-kmp](https://developer.android.com/kotlin/multiplatform/datastore)

- webview: [compose-webview-multiplatform](https://github.com/KevinnZou/compose-webview-multiplatform)




### 开发工具
Android Studio 2024.3.2





### License

```
    Copyright 2024 shuanghua

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```