@file:OptIn(ExperimentalJsExport::class)

package ch.burguiere.carbonlog.model

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
interface CoreCarbonLog<TimestampType>{
    fun getTotalCarbonKgs(): Double
    fun getCurrentYearCarbonKgs(): Double
    fun getRangeCarbonKgs(
        from: TimestampType,
        to: TimestampType,
        inclusive: Boolean = false
    ): Double
}
