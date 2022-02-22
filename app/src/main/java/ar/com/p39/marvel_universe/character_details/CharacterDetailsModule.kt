package ar.com.p39.marvel_universe.character_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ar.com.p39.marvel_universe.network.MarvelService
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object CharacterDetailsModule {
    @Singleton
    fun provideFactory(
        assistedFactory: CharacterDetailsViewModelFactory,
        marvelService: MarvelService,
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return assistedFactory.create(marvelService) as T
        }
    }
}