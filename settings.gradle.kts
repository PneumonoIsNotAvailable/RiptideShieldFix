pluginManagement {
	repositories {
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		mavenCentral()
		gradlePluginPortal()
		maven("https://maven.kikugie.dev/snapshots")
	}
}

// Should match your modid
rootProject.name = "riptide_shield_fix"

plugins {
	id("dev.kikugie.stonecutter") version "0.9.4"
}

stonecutter {
	create(rootProject) {
		fun controlledVersions(vararg versions: String) = versions.forEach {
			if (stonecutter.eval(it, ">=26.1")) {
				version(it).buildscript = "build.noremap.gradle.kts"
			} else {
				version(it).buildscript = "build.remap.gradle.kts"
			}
		}

		controlledVersions("1.20", "1.20.5", "26.1", "26.2")
		vcsVersion = "26.2"
	}
}