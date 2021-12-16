plugins {
    kotlin("jvm") version "1.6.0" apply  false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    repositories {
        mavenCentral()
    }
    val implementation by configurations
    val testImplementation by configurations
    dependencies {
        implementation(kotlin("stdlib"))
        testImplementation(kotlin("test"))
    }

    tasks.withType(Test::class) {
        useJUnitPlatform()
    }

    // Create tasks to generate resources per day
    val srcDir = this.projectDir.toPath().resolve("src/main/kotlin/mri/advent/y2021")
    val testSrcDir = this.projectDir.toPath().resolve("src/test/kotlin/mri/advent/y2021")
    val resourcesDir = this.projectDir.toPath().resolve("src/main/resources")
    val testResourcesDir = this.projectDir.toPath().resolve("src/test/resources")
    val dayTemplate = this.rootProject.projectDir.toPath().resolve("templates/dayDD.kt").toFile().readText()
    val testDayTemplate = this.rootProject.projectDir.toPath().resolve("templates/dayDD-test.kt").toFile().readText()
    (1..25).map { if (it < 10) "0$it" else "$it" }.forEach { day ->
        this.tasks.create("gen-day-$day") {
            group = "advent"
            doLast {
                println("generate day $day")
                // gen input files
                if (!resourcesDir.resolve("day${day}.in").toFile().exists()) {
                    resourcesDir.resolve("day${day}.in").toFile().createNewFile()
                }
                if (!testResourcesDir.resolve("day${day}_sample.in").toFile().exists()) {
                    testResourcesDir.resolve("day${day}_sample.in").toFile().createNewFile()
                }
                // gen class file
                if (!srcDir.resolve("day${day}.kt").toFile().exists()) {
                    srcDir.resolve("day${day}.kt").toFile().writeText(dayTemplate.replace("DD", day))
                }
                // gen test class file
                if (!testSrcDir.resolve("Day${day}Test.kt").toFile().exists()) {
                    testSrcDir.resolve( "Day${day}Test.kt").toFile().writeText(testDayTemplate.replace("DD", day))
                }
            }
        }
    }
}