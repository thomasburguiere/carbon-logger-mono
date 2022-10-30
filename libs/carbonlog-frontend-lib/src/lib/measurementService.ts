import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class MeasurementService {

    constructor(private readonly http: HttpClient) {
    }

    getMeasurements(): Observable<MeasurementDto[]> {
        return this.http.get<MeasurementDto[]>('http://localhost:18080/carbon-logs/measurements', {
            headers: {'Authorization': 'Basic mMLJYyyeDqZiK7sz6WtJsiiBYne8hd2U'}
        });
    }
}

export interface MeasurementDto {
    co2Kg: number;
    dt: string;
}
