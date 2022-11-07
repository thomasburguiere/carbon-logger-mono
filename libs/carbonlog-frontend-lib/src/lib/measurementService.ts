import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
    providedIn: "root"
})
export class MeasurementService {

    constructor(private readonly http: HttpClient) {
    }

    private token = "mMLJYyyeDqZiK7sz6WtJsiiBYne8hd2U";

    private apiUrl = "http://localhost:18080";

    getMeasurements(): Observable<MeasurementDto[]> {
        return this.http.get<MeasurementDto[]>(`${this.apiUrl}/carbon-logs/measurements`, {
            headers: {"Authorization": `Basic ${this.token}`}
        });
    }

    saveMeasurement(co2Kg: number) {
        return this.http.post(`${this.apiUrl}/carbon-logs/measurements/${co2Kg}`, undefined, {
            headers: {"Authorization": `Basic ${this.token}`}
        });
    }
}

export interface MeasurementDto {
    co2Kg: number;
    dt: string;
}
