import ch.burguiere.carbonlog.carbonlogbackend.repository.MongoCarbonMeasurementsRepository;

public class Test {

    static void doSomething() {
        MongoCarbonMeasurementsRepository.getCollectionName();
        final var s = MongoCarbonMeasurementsRepository.Fields.CO2_KG;
    }
}
