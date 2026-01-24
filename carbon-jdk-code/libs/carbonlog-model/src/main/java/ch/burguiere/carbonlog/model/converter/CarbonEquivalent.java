package ch.burguiere.carbonlog.model.converter;

public record CarbonEquivalent(Double co2Kg) {

    public static CarbonEquivalent ofCarKm(final Double carKm) {
        return new CarbonEquivalent(carKm / CO2KgRatios.carKm.getRatio());
    }

    public static CarbonEquivalent ofPlaneKm(final Double planeKm) {
        return new CarbonEquivalent(planeKm / CO2KgRatios.planeKm.getRatio());
    }

    public static CarbonEquivalent ofTrainKm(final Double trainKm) {
        return new CarbonEquivalent(trainKm / CO2KgRatios.trainKm.getRatio());
    }

    public static CarbonEquivalent ofBeefMeal(final Double beefMeal) {
        return new CarbonEquivalent(beefMeal / CO2KgRatios.beefMeal.getRatio());
    }

    public static CarbonEquivalent ofChickenMeal(final Double chickenMeal) {
        return new CarbonEquivalent(chickenMeal / CO2KgRatios.chickenMeal.getRatio());
    }

    public static CarbonEquivalent ofChickenKg(final Double chickenKg) {
        return new CarbonEquivalent(chickenKg / CO2KgRatios.chickenKg.getRatio());
    }

    public static CarbonEquivalent ofEggsKg(final Double eggsKg) {
        return new CarbonEquivalent(eggsKg / CO2KgRatios.eggsKg.getRatio());
    }

    public static CarbonEquivalent ofVeggieMeal(final Double veggieMeal) {
        return new CarbonEquivalent(veggieMeal / CO2KgRatios.veggieMeal.getRatio());
    }

    public static CarbonEquivalent ofTapWaterLiters(final Double tapWaterLiters) {
        return new CarbonEquivalent(tapWaterLiters / CO2KgRatios.tapWaterLiters.getRatio());
    }

    public static CarbonEquivalent ofBottledWaterLiter(final Double bottledWaterLiter) {
        return new CarbonEquivalent(bottledWaterLiter / CO2KgRatios.bottledWaterLiter.getRatio());
    }

    public static CarbonEquivalent ofHeatingDayGas(final Double heatingDayGas) {
        return new CarbonEquivalent(heatingDayGas / CO2KgRatios.heatingDayGas.getRatio());
    }

    public static CarbonEquivalent ofSmartPhone(final Double smartPhone) {
        return new CarbonEquivalent(smartPhone / CO2KgRatios.smartPhone.getRatio());
    }

    public static CarbonEquivalent ofDenimPants(final Double denimPants) {
        return new CarbonEquivalent(denimPants / CO2KgRatios.denimPants.getRatio());
    }

    public static CarbonEquivalent ofPocketBook(final Double pocketBook) {
        return new CarbonEquivalent(pocketBook / CO2KgRatios.pocketBook.getRatio());
    }

    public static CarbonEquivalent ofSofa(final Double sofa) {
        return new CarbonEquivalent(sofa / CO2KgRatios.sofa.getRatio());
    }

    public static CarbonEquivalent ofCottonTShirt(final Double cottonTShirt) {
        return new CarbonEquivalent(cottonTShirt / CO2KgRatios.cottonTShirt.getRatio());
    }

    public static CarbonEquivalent ofLaptop(final Double laptop) {
        return new CarbonEquivalent(laptop / CO2KgRatios.laptop.getRatio());
    }

    public static CarbonEquivalent ofComputer24InchScreen(final Double computer24InchScreen) {
        return new CarbonEquivalent(computer24InchScreen / CO2KgRatios.computer24InchScreen.getRatio());
    }

    public static CarbonEquivalent ofTv45Inch(final Double tv45Inch) {
        return new CarbonEquivalent(tv45Inch / CO2KgRatios.tv45Inch.getRatio());
    }

    public static CarbonEquivalent ofA4PaperSheet(final Double a4PaperSheet) {
        return new CarbonEquivalent(a4PaperSheet / CO2KgRatios.a4PaperSheet.getRatio());
    }

    public static CarbonEquivalent ofHouseTrashKg(final Double houseTrashKg) {
        return new CarbonEquivalent(houseTrashKg / CO2KgRatios.houseTrashKg.getRatio());
    }

    public Double carKm() {
        return this.co2Kg * CO2KgRatios.carKm.getRatio();
    }

    public Double planeKm() {
        return this.co2Kg * CO2KgRatios.planeKm.getRatio();
    }

    public Double trainKm() {
        return this.co2Kg * CO2KgRatios.trainKm.getRatio();
    }

    public Double beefMeal() {
        return this.co2Kg * CO2KgRatios.beefMeal.getRatio();
    }

    public Double chickenMeal() {
        return this.co2Kg * CO2KgRatios.chickenMeal.getRatio();
    }

    public Double chickenKg() {
        return this.co2Kg * CO2KgRatios.chickenKg.getRatio();
    }

    public Double eggsKg() {
        return this.co2Kg * CO2KgRatios.eggsKg.getRatio();
    }

    public Double veggieMeal() {
        return this.co2Kg * CO2KgRatios.veggieMeal.getRatio();
    }

    public Double smartPhone() {
        return this.co2Kg * CO2KgRatios.smartPhone.getRatio();
    }

    public Double laptop() {
        return this.co2Kg * CO2KgRatios.laptop.getRatio();
    }

    public Double computer24InchScreen() {
        return this.co2Kg * CO2KgRatios.computer24InchScreen.getRatio();
    }

    public Double tv45Inch() {
        return this.co2Kg * CO2KgRatios.tv45Inch.getRatio();
    }

    public Double denimPants() {
        return this.co2Kg * CO2KgRatios.denimPants.getRatio();
    }

    public Double pocketBook() {
        return this.co2Kg * CO2KgRatios.pocketBook.getRatio();
    }

    public Double sofa() {
        return this.co2Kg * CO2KgRatios.sofa.getRatio();
    }

    public Double cottonTShirt() {
        return this.co2Kg * CO2KgRatios.cottonTShirt.getRatio();
    }

    public Double a4PaperSheet() {
        return this.co2Kg * CO2KgRatios.a4PaperSheet.getRatio();
    }

    public Double houseTrashKg() {
        return this.co2Kg * CO2KgRatios.houseTrashKg.getRatio();
    }

}
