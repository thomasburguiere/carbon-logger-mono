package ch.burguiere.carbonlog.model.converter;

public enum CO2KgRatios {
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
    ;

    private final double ratio;

    CO2KgRatios(final double ratio) {
        this.ratio = ratio;
    }

    public double getRatio() {
        return ratio;
    }

}
