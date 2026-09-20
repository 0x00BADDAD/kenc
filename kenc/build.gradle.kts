
plugins {
    id("buildlogic.java-application-conventions") // this comes from buildSrc/ and contains test deps
}

dependencies {
    implementation(project(":lexer"))
    implementation(project(":parser"))
    implementation(project(":ast"))
}


application {
    // Define the main class for the application.
    mainClass = "space.unmei.kenc.Kenc"
}

