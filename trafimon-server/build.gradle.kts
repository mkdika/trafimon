import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.1.5.RELEASE"
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	kotlin("jvm") version "1.3.21"
	kotlin("plugin.spring") version "1.3.21"
}

group = "com.github.mkdika"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web") {
		exclude(module = "spring-boot-starter-tomcat")
	}
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-jetty")

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-oauth2-client")
	implementation("org.springframework.security:spring-security-oauth2-jose")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.9.7")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")

	implementation("com.squareup.retrofit2:converter-jackson:2.5.0")
	implementation("com.squareup.retrofit2:converter-scalars:2.5.0")
	implementation("com.squareup.retrofit2:retrofit:2.5.0")
	implementation("com.squareup.okhttp3:logging-interceptor:3.12.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
