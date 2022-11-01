import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {MeasurementDto} from '../measurementService';
import {Store} from '@ngxs/store';
import {FetchMeasurements} from '../store/actions';

@Component({
    selector: 'app-measurements',
    template: `
        Measurements:<br>
        <li *ngFor="let measurement of measurements$ | async">{{measurement.co2Kg}} co2Kg - {{measurement.dt}}</li>
    `,
    styles: [],
})
export class MeasurementsComponent implements OnInit {
    measurements$?: Observable<MeasurementDto[]>;

    constructor(private store: Store) {
        this.measurements$ = this.store.select((state) => state.measurements.values);
    }

    ngOnInit(): void {
        this.store.dispatch(new FetchMeasurements());
    }
}
