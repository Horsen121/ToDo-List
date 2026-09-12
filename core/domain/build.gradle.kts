plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-test-fixtures")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)

    testFixturesApi(libs.kotlinx.coroutines.test)
    testFixturesApi(libs.junit)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}