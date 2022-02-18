package ar.com.p39.marvel_universe.network_models

data class ComicList(
    val available: Int,
    val collectionURI: String,
    val items: List<ComicSummary>,
    val returned: Int
)