package com.example.tradeapp.damin.repository



interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
//    suspend fun signInWithGoogle(): Boolean
}