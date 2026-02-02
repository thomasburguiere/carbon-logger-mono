@file:OptIn(ExperimentalJsExport::class, ExperimentalUuidApi::class, ExperimentalTime::class)

package ch.burguiere.carbonlog.model

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

@JsExport
interface CoreCarbonMeasurement<TimestampType> {
    val id: String
    val co2Kg: Double
    val dt: TimestampType
    val inputDescription: String?
}
