import {Component, Input, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {MeasurementDto, MeasurementService} from '../measurementService';

@Component({
    selector: 'app-measurements',
    template: `
        Measurements:<br>
        <li *ngFor="let measurement of measurements$ | async">{{measurement.co2Kg}} co2Kg - {{measurement.dt}}</li>
    `,
    styles: [],
})
export class MeasurementsComponent implements OnInit {
    constructor(private readonly measurementService: MeasurementService) {
    }

    @Input() measurements$?: Observable<MeasurementDto[]>;

    ngOnInit(): void {
        this.measurements$ = this.measurementService.getMeasurements();
    }
}
