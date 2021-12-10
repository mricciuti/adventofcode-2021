plugins {
    kotlin("jvm") version "1.6.0"
}

dependencies {
    implementation(kotlin("stdlib"))
}

repositories {
    mavenCentral()
}

val srcDir  = projectDir.toPath().resolve("src/main/kotlin/mri/advent2021")
val resourcesDir  = projectDir.toPath().resolve("src/main/resources")
val dayTemplate = projectDir.toPath().resolve("src/main/resources/dayxx.kt").toFile().readText()

(0..25).map { if (it < 10) "0$it" else "$it" }.forEach { day ->
    tasks.create("gen-day-$day") {
        group = "advent2021"
        doLast {
            println("generate day $day")
            // gen input files
            if(!resourcesDir.resolve("day$day.in").toFile().exists()) {
                resourcesDir.resolve("day$day.in").toFile().createNewFile()
                resourcesDir.resolve("day${day}_sample.in").toFile().createNewFile()
            }
            // gen class file
            if(!srcDir.resolve("day$day.kt").toFile().exists()) {
                srcDir.resolve("day$day.kt").toFile().writeText(dayTemplate.replace("xx", day))
            }
        }
    }
}