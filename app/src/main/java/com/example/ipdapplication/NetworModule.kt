package com.example.ipdapplication


import com.example.ipdapplication.auth.AuthInterceptor
import com.example.ipdapplication.auth.TokenStore
import com.example.ipdapplication.auth.api.AuthApi
import com.example.ipdapplication.community.Api.BlogApi
import com.example.ipdapplication.community.Api.BlogCommentApi
import com.example.ipdapplication.community.Api.BlogLikeApi
import com.example.ipdapplication.community.dto.BlogCommentLikeApi
import com.example.ipdapplication.cupon.api.CouponApi
import com.example.ipdapplication.map.api.AddressApi
import com.example.ipdapplication.map.api.CommentApi
import com.example.ipdapplication.map.api.DirectionsApi
import com.example.ipdapplication.map.api.PlaceApi
import com.example.ipdapplication.map.api.PlaceCommentLikeApi
import com.example.ipdapplication.map.api.PlaceLikeApi
import com.example.ipdapplication.led.api.LedApi
import com.example.ipdapplication.map.gcoo.api.GcooApi
import com.example.ipdapplication.map.menu.api.MenuApi
import com.example.ipdapplication.map.review.api.ReviewApi
import com.example.ipdapplication.map.review.api.TagApi
import com.example.ipdapplication.qr.api.QrApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val jsonInstance = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideJson(): Json = jsonInstance


    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenStore: TokenStore): AuthInterceptor =
        AuthInterceptor(tokenStore)

    @Provides
    @Singleton
    fun provideOkHttp(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Provides @Singleton
    fun provideRetrofit(ok: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(
                jsonInstance.asConverterFactory("application/json".toMediaType())
            )
            .client(ok)
            .build()


    @Provides @Singleton
    fun providePlaceApi(retrofit: Retrofit): PlaceApi = retrofit.create(PlaceApi::class.java)

    @Provides @Singleton
    fun provideCommentApi(retrofit: Retrofit): CommentApi = retrofit.create(CommentApi::class.java)

    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideBlogApi(retrofit: Retrofit): BlogApi =
        retrofit.create(BlogApi::class.java)

    @Provides
    @Singleton
    fun provideBlogCommentApi(retrofit: Retrofit): BlogCommentApi =
        retrofit.create(BlogCommentApi::class.java)

    @Provides
    @Singleton
    fun provideDirectionsApi(retrofit: Retrofit): DirectionsApi =
        retrofit.create(DirectionsApi::class.java)

    @Provides
    @Singleton
    fun providePlaceLikeApi(retrofit: Retrofit): PlaceLikeApi =
        retrofit.create(PlaceLikeApi::class.java)

    @Provides
    @Singleton
    fun providePlaceCommentLikeApi(retrofit: Retrofit): PlaceCommentLikeApi =
        retrofit.create(PlaceCommentLikeApi::class.java)

    @Provides
    @Singleton
    fun provideBlogLikeApi(retrofit: Retrofit): BlogLikeApi =
        retrofit.create(BlogLikeApi::class.java)

    @Provides
    @Singleton
    fun provideBlogCommentLikeApi(retrofit: Retrofit): BlogCommentLikeApi =
        retrofit.create(BlogCommentLikeApi::class.java)

    @Provides
    @Singleton
    fun provideAddressApi(retrofit: Retrofit): AddressApi {
        return retrofit.create(AddressApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCouponApi(retrofit: Retrofit): CouponApi =
        retrofit.create(CouponApi::class.java)

    @Provides
    @Singleton
    fun provideQrApi(retrofit: Retrofit): QrApi =
        retrofit.create(QrApi::class.java)

    @Provides
    @Singleton
    fun provideMenuApi(retrofit: Retrofit): MenuApi =
        retrofit.create(MenuApi::class.java)


    @Provides
    @Singleton
    fun provideReviewApi(retrofit: Retrofit): ReviewApi =
        retrofit.create(ReviewApi::class.java)

    @Provides
    @Singleton
    fun provideTagApi(retrofit: Retrofit): TagApi =
        retrofit.create(TagApi::class.java)

    @Provides
    @Singleton
    fun provideGcooApi(retrofit: Retrofit): GcooApi =
        retrofit.create(GcooApi::class.java)

    @Provides
    @Singleton
    fun provideLedApi(retrofit: Retrofit): LedApi =
        retrofit.create(LedApi::class.java)
}
