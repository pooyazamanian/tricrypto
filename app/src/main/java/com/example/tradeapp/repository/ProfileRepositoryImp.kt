package com.example.tradeapp.repository

import com.example.tradeapp.dto.Profile
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
    override suspend fun getProfile(userId: UUID): PostgrestResult? {
        return supabaseClient.get(
            "profiles",
            filter = mapOf("id" to userId)
        )
    }

    override suspend fun upsertProfile(profile: Profile): PostgrestResult {
        return supabaseClient.upsert<Profile>("profiles", profile)
    }

}