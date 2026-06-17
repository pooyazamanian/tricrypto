package com.example.tradeapp.damin.api

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.serialization.json.JsonElement
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach

@Singleton
class SupabaseClientWrapper
@Inject constructor(
    private val client: SupabaseClient
) {

    val db = client.postgrest  // فقط بخش پایگاه داده

    // ---------- Generic GET ----------
    suspend fun get(
        table: String,
        columns: Columns = Columns.ALL,
        filter: Map<String, Any> = emptyMap(),
        orderActive: Boolean = false,
        limit: Long? = null
    ): PostgrestResult {

        return db.from(table).select(columns) {

            filter.forEach { (key, value) ->
                filter {
                    eq(key, value)
                }
            }
            if(orderActive){
                order("timestamp", Order.DESCENDING)
            }


            limit?.let {
                limit(it)
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

    fun observeTable(
        table: String,
        filter: Map<String, Any> = emptyMap()
    ): Flow<PostgresAction> {

        val channel = client.channel(table)

        return channel
            .postgresChangeFlow<PostgresAction>(
                schema = "public"
            ) {
                this.table = table
            }
            .filter { action ->

                if (filter.isEmpty()) {
                    true
                } else {

                    when (action) {

                        is PostgresAction.Insert -> {
                            filter.all { (key, value) ->
                                action.record[key]?.toString()?.contains(value.toString()) == true
                            }
                        }

                        is PostgresAction.Update -> {
                            filter.all { (key, value) ->
                                action.record[key]?.toString()?.contains(value.toString()) == true
                            }
                        }

                        else -> true
                    }
                }
            }
    }

    suspend fun rpc(
        functionName: String,
        params: Map<String, JsonElement>
    ): PostgrestResult {

        return db.rpc(
            function = functionName,
            parameters = params
        )
    }
}