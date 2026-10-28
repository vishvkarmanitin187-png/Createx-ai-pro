package com.example.data

import com.example.base.FirestoreEmulatorTestBase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FirestoreProjectRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun saveAndGetProject_authenticatedUser_success() = runBlocking {
        val aliceUid = signInTestUser("alice_rule@example.com")
        val repository = FirestoreProjectRepository(firestore)

        val project = FirestoreVideoProject(
            id = "test_proj_1",
            userId = aliceUid,
            name = "Robolectric Cyber Reel",
            aspectRatio = "REELS_9_16",
            resolution = "1080p",
            fps = 30L,
            clipsCount = 2L,
            durationSeconds = 12.0,
            thumbnailUri = "content://media/test"
        )

        withTimeout(DEFAULT_TIMEOUT_MS) {
            val savedId = repository.saveProject(aliceUid, project)
            assertEquals("test_proj_1", savedId)

            val retrieved = repository.getProject(aliceUid, "test_proj_1")
            assertNotNull(retrieved)
            assertEquals("Robolectric Cyber Reel", retrieved?.name)
            assertEquals(aliceUid, retrieved?.userId)
        }
    }

    @Test
    fun deleteProject_ownerUser_success() = runBlocking {
        val aliceUid = signInTestUser("alice_delete@example.com")
        val repository = FirestoreProjectRepository(firestore)

        val project = FirestoreVideoProject(
            id = "test_proj_del",
            userId = aliceUid,
            name = "Project to Delete",
            aspectRatio = "REELS_9_16",
            resolution = "720p",
            fps = 30L,
            clipsCount = 1L,
            durationSeconds = 5.0,
            thumbnailUri = ""
        )

        withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.saveProject(aliceUid, project)
            repository.deleteProject(aliceUid, "test_proj_del")

            val afterDelete = repository.getProject(aliceUid, "test_proj_del")
            assertTrue(afterDelete == null)
        }
    }

    @Test
    fun saveUserProfile_authenticatedUser_success() = runBlocking {
        val aliceUid = signInTestUser("alice_profile@example.com")
        val repository = FirestoreProjectRepository(firestore)

        val profile = FirestoreUserProfile(
            uid = aliceUid,
            email = "alice_profile@example.com",
            displayName = "Alice Creator"
        )

        withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.saveUserProfile(profile)
        }
    }
}
