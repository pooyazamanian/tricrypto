package com.example.tradeapp.damin.modul

import com.example.tradeapp.damin.repository.AssetRepository
import com.example.tradeapp.damin.repository.AuthenticationRepository
import com.example.tradeapp.damin.repository.OrderRepository
import com.example.tradeapp.repository.ProfileRepositoryImp
import com.example.tradeapp.damin.repository.ProfileRepository
import com.example.tradeapp.damin.repository.TradeRepository
import com.example.tradeapp.damin.repository.WalletRepository
import com.example.tradeapp.repository.AssetRepositoryImp
import com.example.tradeapp.repository.AuthenticationRepositoryImpl
import com.example.tradeapp.repository.OrderRepositoryImp
import com.example.tradeapp.repository.TradeRepositoryImp
import com.example.tradeapp.repository.WalletRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAssetRepository(
        impl: AssetRepositoryImp
    ): AssetRepository

    @Binds
    abstract fun bindAuthenticationRepository(
        impl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    abstract fun bindOrderRepository(
        impl: OrderRepositoryImp
    ): OrderRepository

    @Binds
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImp
    ): ProfileRepository

    @Binds
    abstract fun bindTradeRepository(
        impl: TradeRepositoryImp
    ): TradeRepository

    @Binds
    abstract fun bindWalletRepository(
        impl: WalletRepositoryImp
    ): WalletRepository

}
