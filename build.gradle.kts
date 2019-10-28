import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    `maven-publish`
}

group = "com.teheidoma"
version = "1.0"

repositories {
    mavenCentral()
}

//val sourcesJar by tasks.registering(Jar::class) {
//    //classifier = "sources"
//    from(sourceSets.main.get().allSource)
//}

publishing{
    repositories{
        maven{
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/teheidoma/snowflake")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GPR_API_KEY")
            }
        }
    }
    publications {
        register("gpr", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("commons-net:commons-net:3.6")
    implementation("org.slf4j:slf4j-api:1.7.25")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}