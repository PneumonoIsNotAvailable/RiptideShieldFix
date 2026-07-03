plugins {
	id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
	`maven-publish`
	id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}

version = "${property("mod_version")}+${stonecutter.current.project}+${property("mod_subversion")}"
base.archivesName.set("${property("mod_id")}")
group = "net.pneumono.${property("mod_id")}"

repositories {

}

dependencies {
	minecraft("com.mojang:minecraft:${stonecutter.current.version}")
	implementation("net.fabricmc:fabric-loader:${property("loader_version")}")

	// Fabric API
	implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
}

tasks.processResources {
	val version = version
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand(mapOf(
			"version" to project.property("mod_version"),
			"supported_versions" to project.property("supported_version_range"),
			"java" to ">=25"
		))
	}

	filesMatching("riptide_shield_fix.mixins.json") {
		expand(
			"java" to "25"
		)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

publishMods {
	file = tasks.jar.map { it.archiveFile.get() }
	additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })
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

	if (stonecutter.current.project == "26.2") {
		discord {
			webhookUrl = discordToken

			username = "Riptide Shield Fix Updates"

			avatarUrl = "https://github.com/PneumonoIsNotAvailable/RiptideShieldFix/blob/master/src/main/resources/assets/riptide_shield_fix/icon.png?raw=true"

			content = changelog.map { "# Riptide Shield Fix version ${project.version}\n<@&1472490332783378472>\n" + it }
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
