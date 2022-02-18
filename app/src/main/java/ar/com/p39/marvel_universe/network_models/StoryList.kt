package ar.com.p39.marvel_universe.network_models

data class StoryList(
    val available: Int,
    val collectionURI: String,
    val items: List<StorySummary>,
    val returned: Int
)