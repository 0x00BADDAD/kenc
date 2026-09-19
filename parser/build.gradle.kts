import org.gradle.api.tasks.testing.Test


plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lexer"))
    implementation(project(":ast"))
    implementation(project(":semant"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Test>().configureEach {
    reports {
        html.required.set(true)
    }
}

