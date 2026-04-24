package com.example.tradeapp.damin.api

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.result.PostgrestResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseClientWrapper
@Inject constructor(
    private val client: SupabaseClient
) {

    val db = client.postgrest  // فقط بخش پایگاه داده

    // ---------- Generic GET ----------
    suspend fun get(
        table: String,
        columns: Columns = Columns.ALL,       // مثال: "*,profile(*)"
        filter: Map<String, Any> = emptyMap()
    ): PostgrestResult {
        return db.from(table).select(columns) {
            if(filter.isNotEmpty()){
                filter.forEach { (key, value) ->
                    filter {
                        eq(key, value)
                    }
                }
            }
        }
    }

    // ---------- Generic POST (insert) ----------
    suspend inline fun <reified T: Any> post(
        table: String,
        data: T
    ): PostgrestResult {
        return db.from(table).insert(data)
    }


    suspend inline fun <reified T: Any> upsert(
        table: String,
        data: T
    ): PostgrestResult {
        return db.from(table).upsert(data)
    }

    // ---------- Generic PATCH (update) ----------
    suspend inline fun <reified T: Any> patch(
        table: String,
        filter: Map<String, Any> = emptyMap(),
        data: T
    ): PostgrestResult {
        return db.from(table).update(data) {
            filter {
                filter.forEach { (key, value) ->
                    eq(key, value)
                }
            }
        }
    }

    // ---------- Generic DELETE ----------
    suspend fun delete(
        table: String,
        filter: Map<String, Any> = emptyMap()
    ): PostgrestResult {
        return db.from(table).delete {
            filter {
                filter.forEach { (key, value) ->
                    eq(key, value)
                }
            }
        }
    }
}