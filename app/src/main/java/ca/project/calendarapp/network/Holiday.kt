package ca.project.calendarapp.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Holiday(
    val date: String,
    @SerialName(value = "localName")
    val localName: String,
    val name: String,
    @SerialName(value = "countryCode")
    val countryCode: String,
    val fixed: Boolean,
    val global: Boolean,
    val counties: List<String>?,
    @SerialName(value = "launchYear")
    val launchYear: Int?,
    val types: List<String>
)
