package ar.com.p39.marvel_universe.network

import android.content.Context
import android.util.Log
import ar.com.p39.marvel_universe.BuildConfig
import ar.com.p39.marvel_universe.network_models.*
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MarvelServiceModule::class]
)
class FakeMarvelServiceModule {
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
        val apiKey = BuildConfig.API_KEY
        val privateKey = BuildConfig.API_SECRET
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
        return FakeMarvenService()
    }

    inner class FakeMarvenService : MarvelService {
        private val character = Character(
            name = "Hulk",
            comics = ComicList(
                available = 0,
                items = emptyList(),
                returned = 0,
                collectionURI = "",
            ),
            events = EventList(
                available = 0,
                items = emptyList(),
                returned = 0,
                collectionURI = "",
            ),
            series = SeriesList(
                available = 0,
                items = emptyList(),
                returned = 0,
                collectionURI = "",
            ),
            description = "Hulk is green",
            id = "hulk",
            modified = "NOW",
            resourceURI = "http://example.com/",
            stories = StoryList(
                available = 0,
                items = emptyList(),
                returned = 0,
                collectionURI = "",
            ),
            thumbnail = Thumbnail("", "jpg"),
            urls = emptyList(),
        )
        private val characterDataContainer = CharacterDataContainer(
            characters = listOf(character),
            count = 1,
            total = 1,
            offset = 0,
            limit = 20,
        )
        private val characterDataWrapper = CharacterDataWrapper(
            code = "200",
            attributionHTML = "TEST",
            attributionText = "TEST",
            characterData = characterDataContainer,
            copyright = "Tests",
            etag = "none",
            status = "OK",
        )

        override suspend fun getCharacters(
            nameStartsWith: String?,
            limit: Int,
            offset: Int
        ): CharacterDataWrapper = characterDataWrapper

        override suspend fun getCharacter(characterId: String): CharacterDataWrapper =
            characterDataWrapper
    }
}