package com.example.tradeapp.damin.modul

import android.content.Context
import android.content.res.Resources
import androidx.annotation.UiContext
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton
import com.example.tradeapp.R
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

@InstallIn(SingletonComponent::class)
@Module
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(@ApplicationContext context: Context): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://nvggjweiwyaaswounyey.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im52Z2dqd2Vpd3lhYXN3b3VueWV5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTA3NDQyODksImV4cCI6MjA2NjMyMDI4OX0.LrC4D7BaCTPmPckMTThSGIk6aLt3jaTT5h2s11FvwmQ"
//            supabaseUrl = "https://ouujizpxpwaqgzpqldvo.supabase.co",
//            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im91dWppenB4cHdhcWd6cHFsZHZvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjEwNDkzMTYsImV4cCI6MjA3NjYyNTMxNn0.eK88dVY-p57Qcj5YkziyZYM8hs_Rz7k9ZrZtlI8wKzA"



        ) {
            install(Postgrest)
            install(Auth) {
                flowType = FlowType.PKCE
                autoSaveToStorage = true
                autoLoadFromStorage = true
                scheme = "app"
                host = "supabase.com"
            }
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(client: SupabaseClient): Auth {
        return client.auth
    }


    @Provides
    @Singleton
    fun provideSupabaseStorage(client: SupabaseClient): Storage {
        return client.storage
    }
}