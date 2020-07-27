package com.releaseshub.gradle.plugin.task

import com.releaseshub.gradle.plugin.artifacts.ArtifactUpgrade
import com.releaseshub.gradle.plugin.common.ResourceUtils
import org.junit.Assert
import org.junit.Test
import java.io.File

class DependenciesExtractorTest {

    @Test
    fun extractFromEmptyTest() {
        val dependenciesParserResult = extractArtifacts("empty_dependencies_file")
        Assert.assertTrue(dependenciesParserResult.getAllArtifacts().isEmpty())
        Assert.assertTrue(dependenciesParserResult.excludedArtifacts.isEmpty())
    }

    @Test
    fun extractFromCommentTest() {
        val dependenciesParserResult = extractArtifacts("comments_file")
        Assert.assertTrue("Found these artifacts: ${dependenciesParserResult.getAllArtifacts()}", dependenciesParserResult.getAllArtifacts().isEmpty())
        Assert.assertTrue(dependenciesParserResult.excludedArtifacts.isEmpty())
    }

    @Test
    fun extractWithVersionTest() {
        val dependenciesParserResult = extractArtifacts("dependencies_versions_file")
        Assert.assertEquals(2, dependenciesParserResult.getAllArtifacts().size)

        val firstArtifact = dependenciesParserResult.getAllArtifacts()[0]
        Assert.assertEquals("com.jdroidtools", firstArtifact.groupId)
        Assert.assertEquals("jdroid-java-core", firstArtifact.artifactId)
        Assert.assertEquals("3.0.0", firstArtifact.fromVersion)

        val secondArtifact = dependenciesParserResult.getAllArtifacts()[1]
        Assert.assertEquals("junit", secondArtifact.groupId)
        Assert.assertEquals("junit", secondArtifact.artifactId)
        Assert.assertEquals("4.13", secondArtifact.fromVersion)

        Assert.assertTrue(dependenciesParserResult.excludedArtifacts.isEmpty())
    }

    @Test
    fun extractWithPlaceholdersTest() {
        val dependenciesParserResult = extractArtifacts("placeholders_dependencies_file")
        Assert.assertEquals(5, dependenciesParserResult.getAllArtifacts().size)

        var artifact = dependenciesParserResult.getAllArtifacts()[0]
        Assert.assertEquals("com.google.firebase", artifact.groupId)
        Assert.assertEquals("firebase-analytics", artifact.artifactId)
        Assert.assertEquals("1.0.0", artifact.fromVersion)

        artifact = dependenciesParserResult.getAllArtifacts()[1]
        Assert.assertEquals("com.google.firebase", artifact.groupId)
        Assert.assertEquals("firebase-messaging", artifact.artifactId)
        Assert.assertEquals("16.0.0", artifact.fromVersion)

        artifact = dependenciesParserResult.getAllArtifacts()[2]
        Assert.assertEquals("com.releaseshub", artifact.groupId)
        Assert.assertEquals("releases-hub-gradle-plugin", artifact.artifactId)
        Assert.assertEquals("1.4.0", artifact.fromVersion)

        artifact = dependenciesParserResult.getAllArtifacts()[3]
        Assert.assertEquals("org.jetbrains.kotlin", artifact.groupId)
        Assert.assertEquals("kotlin-gradle-plugin", artifact.artifactId)
        Assert.assertEquals("1.3.40", artifact.fromVersion)

        artifact = dependenciesParserResult.getAllArtifacts()[4]
        Assert.assertEquals("org.jetbrains.kotlin", artifact.groupId)
        Assert.assertEquals("kotlin-stdlib-jdk7", artifact.artifactId)
        Assert.assertEquals("1.3.40", artifact.fromVersion)

        Assert.assertTrue(dependenciesParserResult.excludedArtifacts.isEmpty())
    }

    @Test
    fun extractGradleArtifactTest() {

        val dependenciesParserResult = extractGradleArtifacts()
        Assert.assertEquals(1, dependenciesParserResult.getAllArtifacts().size)

        val firstArtifact = dependenciesParserResult.getAllArtifacts()[0]
        Assert.assertEquals(ArtifactUpgrade.GRADLE_ID, firstArtifact.id)
        Assert.assertEquals("6.0.1", firstArtifact.fromVersion)

        Assert.assertTrue(dependenciesParserResult.excludedArtifacts.isEmpty())
    }

    private fun extractArtifacts(basePath: String): DependenciesExtractorResult {
        return DependenciesExtractor.extractArtifacts(File(ResourceUtils.getRequiredResourcePath("root")),
            basePath, listOf("Libs.kt"), emptyList(), emptyList())
    }

    private fun extractGradleArtifacts(): DependenciesExtractorResult {
        return DependenciesExtractor.extractArtifacts(File(ResourceUtils.getRequiredResourcePath("root_gradle")),
            "", emptyList(), emptyList(), emptyList())
    }
}
