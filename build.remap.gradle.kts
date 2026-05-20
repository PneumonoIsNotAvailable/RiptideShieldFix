plugins {
	id("net.fabricmc.fabric-loom-remap") version "1.16-SNAPSHOT"
	`maven-publish`
	id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}

version = "${property("mod_version")}+${stonecutter.current.project}+${property("mod_subversion")}"
group = "${property("maven_group")}"

repositories {

}

dependencies {
	minecraft("com.mojang:minecraft:${stonecutter.current.version}")
	mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

	// Fabric API
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
	
}

tasks.processResources {
	val version = version
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand(mapOf(
			"version" to project.property("mod_version"),
			"supported_versions" to project.property("supported_version_range"),
			"java" to if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) ">=21" else ">=17"
		))
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) 21 else 17
}

java {
	withSourcesJar()

	val javaVersion = if (stonecutter.eval(stonecutter.current.version, ">=1.20.5"))
		JavaVersion.VERSION_21 else JavaVersion.VERSION_17

	sourceCompatibility = javaVersion
	targetCompatibility = javaVersion
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

publishMods {
	file = tasks.remapJar.get().archiveFile
	additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)
	displayName = "Riptide Shield Fix ${project.version}"
	version = "${project.version}"
	changelog = rootProject.file("CHANGELOG.md").readText()
	type = STABLE
	modLoaders.addAll("fabric", "quilt")

	val modrinthToken = providers.environmentVariable("MODRINTH_TOKEN")
	val discordToken = providers.environmentVariable("DISCORD_TOKEN")

	dryRun = modrinthToken.getOrNull() == null || discordToken.getOrNull() == null

	modrinth {
		accessToken = modrinthToken
		projectId = "pu0BaLmL"

		minecraftVersionRange {
			start = "${property("min_supported_version")}"
			end = "${property("max_supported_version")}"
		}

		requires {
			// Fabric API
			id = "P7dR8mSH"
		}
	}
}

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}
