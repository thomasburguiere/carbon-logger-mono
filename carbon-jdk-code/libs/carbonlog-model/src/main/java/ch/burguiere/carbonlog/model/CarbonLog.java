package ch.burguiere.carbonlog.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record CarbonLog(List<CarbonMeasurement> carbonMeasurements) {

    Double getRangeCarbonKgs(final Instant from, final Instant to, final Boolean inclusive) {

        final Predicate<CarbonMeasurement> filter;
        if (!inclusive) {
            filter = (cm) -> cm.dt().isAfter(from) && cm.dt().isBefore(to);
        } else {
            filter = (cm) -> (cm.dt().equals(from) || cm.dt().isAfter(from)) && (cm.dt().isBefore(to) || cm.dt().equals(to));
        }

        final var filtered = carbonMeasurements.stream()
                .filter(filter)
                .toList();
        return sumCO2kgs(filtered);
    }

    public Double getRangeCarbonKgs(final Instant from, final Instant to) {
        return getRangeCarbonKgs(from, to, false);
    }

    public CarbonLog copyAdding(final CarbonMeasurement measurement) {
        return copyAddingMultiple(List.of(measurement));
    }

    public CarbonLog copyAddingMultiple(final List<CarbonMeasurement> measurements) {
        return new CarbonLog(Stream.concat(this.carbonMeasurements.stream(), measurements.stream()).toList());
    }

    private static Double sumCO2kgs(final List<CarbonMeasurement> measurements) {
        if (measurements.isEmpty()) {
            return 0.0;
        }
        return measurements.stream()
                .map(CarbonMeasurement::co2Kg)
                .reduce(0.0, Double::sum);
    }

    public Double getCurrentYearCarbonKgs() {
        return sumCO2kgs(
                carbonMeasurements.stream()
                        .filter(cm -> cm.getYear().equals(LocalDate.now().getYear()))
                        .toList()
        );
    }

    public Double getTotalCarbonKgs() {
        return sumCO2kgs(carbonMeasurements);
    }
}
