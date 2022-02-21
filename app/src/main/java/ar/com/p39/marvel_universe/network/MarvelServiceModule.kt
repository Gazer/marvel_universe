package ar.com.p39.marvel_universe.network

import android.content.Context
import android.util.Log
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MarvelServiceModule {
    @Provides
    @Singleton
    fun providePicasso(@ApplicationContext context: Context): Picasso {
        val builderPicasso = OkHttpClient.Builder()
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))

        return Picasso.Builder(context)
            .downloader(OkHttp3Downloader(builderPicasso.build()))
            .listener { _, _, exception -> exception.message?.let { Log.e("Picasso", it) } }
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // TODO: Move to external file
        val apiKey = "customApiKey"
        val privateKey = "customApiSecrect"
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        return OkHttpClient().newBuilder()
            .addInterceptor(MarvelSignRequestInterceptor(apiKey, privateKey))
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideMarvelService(okHttpClient: OkHttpClient): MarvelService {
        return Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MarvelService::class.java)
    }
}