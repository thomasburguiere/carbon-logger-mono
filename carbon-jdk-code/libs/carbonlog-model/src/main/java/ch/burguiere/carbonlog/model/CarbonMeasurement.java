package ch.burguiere.carbonlog.model;

import ch.burguiere.carbonlog.model.converter.CarbonEquivalent;
import org.immutables.value.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;


@Value.Builder
public record CarbonMeasurement(
        String id,
        Double co2Kg,
        Instant dt,
        Optional<String> inputDescription
) {
    public static class Builder extends CarbonMeasurementBuilder {
        public Builder() {
            id(UUID.randomUUID().toString());
            inputDescription(Optional.empty());
            dt(Instant.now());
        }
    }

    public static CarbonMeasurement of(final Double carbonKg) {
        return new CarbonMeasurement.Builder()
                .co2Kg(carbonKg)
                .build();
    }

    public static CarbonMeasurement ofCarbonEquivalent(final CarbonEquivalent equivalent) {
        return new CarbonMeasurement.Builder()
                .co2Kg(equivalent.co2Kg())
                .build();
    }

    public CarbonEquivalent asCarbonEquivalent() {
        return new CarbonEquivalent(co2Kg);
    }

    Integer getYear() {
        return LocalDate.ofInstant(dt, ZoneId.of("UTC")).getYear();
    }
}
