package com.example.tradeapp.repository

import com.example.tradeapp.damin.model.Profile
import com.example.tradeapp.damin.repository.ProfileRepository
import com.example.tradeapp.damin.api.SupabaseClientWrapper
import io.github.jan.supabase.postgrest.result.PostgrestResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper

) : ProfileRepository {
    // ---------- Profiles ----------
    override suspend fun getProfile(userId: UUID): PostgrestResult? = try {
        supabaseClient.get(
            "profiles",
            filter = mapOf("id" to userId)
        )
    } catch (e: Exception) {
        null
    }

    override suspend fun upsertProfile(profile: Profile): PostgrestResult? = try {
        supabaseClient.upsert<Profile>("profiles", profile)
    } catch (e: Exception) {
        null
    }

}