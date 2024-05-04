plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-web:3.2.5")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.5")
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.5")
    implementation("org.springframework.security:spring-security-config:version")
    implementation("io.jsonwebtoken:jjwt:0.12.5")
    implementation("javax.xml.bind:jaxb-api:2.3.1")

    implementation("org.mapstruct:mapstruct:1.5.5.Final")

    runtimeOnly ("org.postgresql:postgresql:42.7.3")

    compileOnly ("org.projectlombok:lombok:1.18.32")

    annotationProcessor ("org.projectlombok:lombok:1.18.32")
    annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.5.Final")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}