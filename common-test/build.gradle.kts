plugins {
}

    android {
    namespace = "com.brunodegan.common_test"

    defaultConfig {
      applicationId = "com.brunodegan.common_test"
    minSdk = 24
    targetSdk = 37
    versionCode = 1
    versionName = "1.0"

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    }

  dependencies {
  }