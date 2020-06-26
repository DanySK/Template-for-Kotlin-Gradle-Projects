plugins {
    `java-library`
    kotlin("multiplatform")
    `maven-publish`
    signing
    id("org.danilopianini.git-sensitive-semantic-versioning")
}

apply(plugin = "org.danilopianini.git-sensitive-semantic-versioning")

group = "org.danilopianini"
gitSemVer {
    version = computeGitSemVer()
}

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        jvm {
            compilations {
                get("main").defaultSourceSet {
                    dependencies {
                        implementation(kotlin("stdlib-jdk8"))
                    }
                }
                get("test").defaultSourceSet {
                    dependencies {
                        implementation("io.kotest:kotest-runner-junit5:_")
                        implementation("io.kotest:kotest-assertions-core-jvm:_")
                        implementation("org.mockito:mockito-core:_")
                    }
                }
            }
            mavenPublication {
                artifactId = project.name + "-jvm"
            }
        }

        js {
            nodejs()
            compilations {
                get("main").defaultSourceSet {
                    dependencies {
                        implementation(kotlin("stdlib-js"))
                    }
                }
                get("test").defaultSourceSet {
                    dependencies {
                        implementation(kotlin("test-js"))
                    }
                }
            }
            mavenPublication {
                artifactId = project.name + "-js"
            }
        }

        targets.all {
            compilations.all {
                kotlinOptions {
                    allWarningsAsErrors = true
                    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses", "-Xopt-in=kotlin.RequiresOptIn")
                }
            }
        }
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                developers {
                    developer {
                        name.set("Danilo Pianini")
                        email.set("danilo.pianini@gmail.com")
                        url.set("http://www.danilopianini.org/")
                    }
                }
            }
        }
    }
}
