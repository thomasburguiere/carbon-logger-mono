export class FetchMeasurements {
    static readonly type = '[Measurements] Fetch';
}

export class SaveMeasurement {
    static readonly type = '[Measurements] Save';
    constructor(public co2Kg: number) {
    }
}
