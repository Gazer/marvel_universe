package ar.com.p39.marvel_universe.network_models

data class SeriesList(
    val available: Int,
    val collectionURI: String,
    val items: List<SeriesSummary>,
    val returned: Int
)