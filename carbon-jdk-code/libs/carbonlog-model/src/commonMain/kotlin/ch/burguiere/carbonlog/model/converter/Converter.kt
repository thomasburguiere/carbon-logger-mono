@file:OptIn(ExperimentalJsExport::class)

package ch.burguiere.carbonlog.model.converter

import ch.burguiere.carbonlog.model.CoreCarbonMeasurement
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
enum class CO2KgRatios(val ratio: Double) {
    beefMeal(0.14), chickenMeal(0.63), chickenKg(0.05494505495), // based on https://www.co2everything.com/co2e-of/chicken
    eggsKg(0.1886792453), // based on https://www.co2everything.com/co2e-of/eggs
    veggieMeal(1.96), tapWaterLiters(7575.76), bottledWaterLiter(2.21),

    // Travel
    planeKm(5.38), trainKm(578.8), carKm(5.18),

    // HighTech
    smartPhone(0.03), laptop(0.006), computer24InchScreen(0.004), tv45Inch(0.003),

    // Misc
    denimPants(0.043), pocketBook(0.085), sofa(0.005), cottonTShirt(0.019), a4PaperSheet(218.34), houseTrashKg(2.59), heatingDayGas(
        0.54
    ),
}

@JsExport
fun CoreCarbonMeasurement.asCarbonEquivalent(): CarbonEquivalent = CarbonEquivalent(this.co2Kg)

@JsExport
data class CarbonEquivalent(val co2Kg: Double) {
    companion object {
        fun ofCarKm(carKm: Double): CarbonEquivalent = CarbonEquivalent(carKm / CO2KgRatios.carKm.ratio)

        fun ofPlaneKm(planeKm: Double): CarbonEquivalent = CarbonEquivalent(planeKm / CO2KgRatios.planeKm.ratio)

        fun ofTrainKm(trainKm: Double): CarbonEquivalent = CarbonEquivalent(trainKm / CO2KgRatios.trainKm.ratio)

        fun ofBeefMeal(beefMeal: Double): CarbonEquivalent = CarbonEquivalent(beefMeal / CO2KgRatios.beefMeal.ratio)

        fun ofChickenMeal(chickenMeal: Double): CarbonEquivalent =
            CarbonEquivalent(chickenMeal / CO2KgRatios.chickenMeal.ratio)

        fun ofChickenKg(chickenKg: Double): CarbonEquivalent = CarbonEquivalent(chickenKg / CO2KgRatios.chickenKg.ratio)

        fun ofEggsKg(eggsKg: Double): CarbonEquivalent = CarbonEquivalent(eggsKg / CO2KgRatios.eggsKg.ratio)

        fun ofVeggieMeal(veggieMeal: Double): CarbonEquivalent =
            CarbonEquivalent(veggieMeal / CO2KgRatios.veggieMeal.ratio)

        fun ofTapWaterLiters(tapWaterLiters: Double): CarbonEquivalent =
            CarbonEquivalent(tapWaterLiters / CO2KgRatios.tapWaterLiters.ratio)

        fun ofBottledWaterLiter(bottledWaterLiter: Double): CarbonEquivalent =
            CarbonEquivalent(bottledWaterLiter / CO2KgRatios.bottledWaterLiter.ratio)

        fun ofHeatingDayGas(heatingDayGas: Double): CarbonEquivalent =
            CarbonEquivalent(heatingDayGas / CO2KgRatios.heatingDayGas.ratio)

        fun ofSmartPhone(smartPhone: Double): CarbonEquivalent =
            CarbonEquivalent(smartPhone / CO2KgRatios.smartPhone.ratio)

        fun ofDenimPants(denimPants: Double): CarbonEquivalent =
            CarbonEquivalent(denimPants / CO2KgRatios.denimPants.ratio)

        fun ofPocketBook(pocketBook: Double): CarbonEquivalent =
            CarbonEquivalent(pocketBook / CO2KgRatios.pocketBook.ratio)

        fun ofSofa(sofa: Double): CarbonEquivalent = CarbonEquivalent(sofa / CO2KgRatios.sofa.ratio)

        fun ofCottonTShirt(cottonTShirt: Double): CarbonEquivalent =
            CarbonEquivalent(cottonTShirt / CO2KgRatios.cottonTShirt.ratio)

        fun ofLaptop(laptop: Double): CarbonEquivalent = CarbonEquivalent(laptop / CO2KgRatios.laptop.ratio)

        fun ofComputer24InchScreen(computer24InchScreen: Double): CarbonEquivalent =
            CarbonEquivalent(computer24InchScreen / CO2KgRatios.computer24InchScreen.ratio)

        fun ofTv45Inch(tv45Inch: Double): CarbonEquivalent = CarbonEquivalent(tv45Inch / CO2KgRatios.tv45Inch.ratio)

        fun ofA4PaperSheet(a4PaperSheet: Double): CarbonEquivalent =
            CarbonEquivalent(a4PaperSheet / CO2KgRatios.a4PaperSheet.ratio)

        fun ofHouseTrashKg(houseTrashKg: Double): CarbonEquivalent =
            CarbonEquivalent(houseTrashKg / CO2KgRatios.houseTrashKg.ratio)
    }

    fun carKm(): Double = this.co2Kg * CO2KgRatios.carKm.ratio

    fun planeKm(): Double = this.co2Kg * CO2KgRatios.planeKm.ratio

    fun trainKm(): Double = this.co2Kg * CO2KgRatios.trainKm.ratio

    fun beefMeal(): Double = this.co2Kg * CO2KgRatios.beefMeal.ratio

    fun chickenMeal(): Double = this.co2Kg * CO2KgRatios.chickenMeal.ratio

    fun chickenKg(): Double = this.co2Kg * CO2KgRatios.chickenKg.ratio

    fun eggsKg(): Double = this.co2Kg * CO2KgRatios.eggsKg.ratio

    fun veggieMeal(): Double = this.co2Kg * CO2KgRatios.veggieMeal.ratio

    fun smartPhone(): Double = this.co2Kg * CO2KgRatios.smartPhone.ratio

    fun laptop(): Double = this.co2Kg * CO2KgRatios.laptop.ratio

    fun computer24InchScreen(): Double = this.co2Kg * CO2KgRatios.computer24InchScreen.ratio

    fun tv45Inch(): Double = this.co2Kg * CO2KgRatios.tv45Inch.ratio

    fun denimPants(): Double = this.co2Kg * CO2KgRatios.denimPants.ratio

    fun pocketBook(): Double = this.co2Kg * CO2KgRatios.pocketBook.ratio

    fun sofa(): Double = this.co2Kg * CO2KgRatios.sofa.ratio

    fun cottonTShirt(): Double = this.co2Kg * CO2KgRatios.cottonTShirt.ratio

    fun a4PaperSheet(): Double = this.co2Kg * CO2KgRatios.a4PaperSheet.ratio

    fun houseTrashKg(): Double = this.co2Kg * CO2KgRatios.houseTrashKg.ratio
}
