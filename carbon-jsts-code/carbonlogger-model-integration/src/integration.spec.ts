import {
    CarbonEquivalent,
    CarbonLog,
    CarbonMeasurement, asCarbonEquivalent
} from "carbonlog-model";

import {describe, expect, it} from 'vitest'

describe('Carbon Model Integration Tests', () => {
    describe('CarbonMeasurement and CarbonEquivalent', () => {
        it('should work', () => {
            const carbonEq = CarbonEquivalent.Companion.ofPlaneKm(2000);
            const measurement = CarbonMeasurement.Companion.ofCarbonEquivalent(carbonEq);

            expect(measurement.co2Kg).closeTo(371.75, 0.01);

            expect(asCarbonEquivalent(measurement).planeKm()).closeTo(2000, 0.01);
        });
    });

    describe('CarbonLog', () => {
        const date1Iso = "2022-01-01T00:00:00.000Z";
        const date2Iso = "2022-02-01T00:00:00.000Z";
        const date3Iso = "2022-03-01T00:00:00.000Z";
        const date4Iso = "2022-04-01T00:00:00.000Z";
        const date_2021_iso = "2021-01-04T00:00:00.000Z";

        it('should get results when in range', () => {
            const cm2 = new CarbonMeasurement('cm2', 2.0, date2Iso)
            const cm3 = new CarbonMeasurement('cm3', 3.0, date3Iso)
            const log = new CarbonLog([cm2, cm3])

            // when
            const result = log.getRangeCarbonKgs(date1Iso, date4Iso)

            // then
            expect(result).equals(5.0)
        });

        it('should get results when in range with added data', () => {
            const cm2 = new CarbonMeasurement('cm2', 2.0, date2Iso)
            const cm3 = new CarbonMeasurement('cm3', 3.0, date3Iso)

            let log = new CarbonLog([cm2, cm3])

            const addedCm = new CarbonMeasurement('added', 11.0, date2Iso)

            log = log.copyAdding(addedCm)

            // when
            const result = log.getRangeCarbonKgs(date1Iso, date4Iso)

            // then
            expect(result).equals(16.0)
        });
    });
});
