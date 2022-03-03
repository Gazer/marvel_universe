package ar.com.p39.marvel_universe.character_list.di

import ar.com.p39.marvel_universe.character_list.pagination.CharactersPagingSource
import ar.com.p39.marvel_universe.character_list.pagination.CharactersRemoteDataSource
import ar.com.p39.marvel_universe.character_list.pagination.CharactersRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CharactersViewModule {
    @Provides
    fun provideCharactersViewModel(charactersPagingSource: CharactersPagingSource): CharactersRemoteDataSource {
        return CharactersRemoteDataSourceImpl(charactersPagingSource)
    }
}