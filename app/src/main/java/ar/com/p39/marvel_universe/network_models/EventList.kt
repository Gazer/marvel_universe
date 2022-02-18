package ar.com.p39.marvel_universe.network_models

data class EventList(
    val available: Int,
    val collectionURI: String,
    val items: List<EventSummary>,
    val returned: Int
)