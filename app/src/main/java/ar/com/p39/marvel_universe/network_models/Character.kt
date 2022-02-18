package ar.com.p39.marvel_universe.network_models

data class Character(
    val comics: ComicList,
    val description: String,
    val events: EventList,
    val id: String,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: SeriesList,
    val stories: StoryList,
    val thumbnail: Thumbnail,
    val urls: List<Url>
)