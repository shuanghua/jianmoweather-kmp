import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties

android {
	namespace = "dev.shuanghua.weather"

	defaultConfig {
		applicationId = "dev.shuanghua.weather"
	}

	buildFeatures {
		buildConfig = true
	}

	compileOptions{
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}


	signingConfigs {
		getByName("debug") {
			val keystorePropertiesFile = "keystore/debug_keystore.properties"
			if (rootProject.file(keystorePropertiesFile).exists()) {
				val keystorePropertiesPath: String = rootProject.file(keystorePropertiesFile).path
				val keystoreProperties: Properties = loadProperties(keystorePropertiesPath)
				storeFile = rootProject.file(keystoreProperties["storeFile"].toString())
				keyAlias = keystoreProperties["keyAlias"].toString()
				keyPassword = keystoreProperties["keyPassword"].toString()
				storePassword = keystoreProperties["storePassword"].toString()
			}
		}

		create("release") { // 创建一个 release 或其它的版本 ，下面的 buildTypes 就能根据创建的名字来获取
			val keystorePropertiesFile = "keystore/release_keystore.properties"
			if (rootProject.file(keystorePropertiesFile).exists()) {
				val keystorePropertiesPath: String = rootProject.file(keystorePropertiesFile).path
				val keystoreProperties: Properties = loadProperties(keystorePropertiesPath)
				storeFile = rootProject.file(keystoreProperties["storeFile"].toString())
				keyAlias = keystoreProperties["keyAlias"].toString()
				keyPassword = keystoreProperties["keyPassword"].toString()
				storePassword = keystoreProperties["storePassword"].toString()
			}
		}
	}

	buildTypes {
		debug {
			signingConfig = signingConfigs.getByName("debug")
		}

		release {
//          请替换你自己的正式签名,并且将 sha-256 散列填到高德定位后台
//			signingConfig = signingConfigs.getByName("release")
			signingConfig = signingConfigs.getByName("debug")
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
}