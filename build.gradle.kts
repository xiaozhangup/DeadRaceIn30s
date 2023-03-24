plugins {
    id("java")
}

group = "me.xiaozhangup.race"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.jar.configure {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest.attributes["Main-Class"] = "me.xiaozhangup.race.CountdownGUI"
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(16))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}