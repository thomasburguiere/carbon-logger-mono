import {Component, Input, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {MeasurementDto, MeasurementService} from '../measurementService';

@Component({
    selector: 'app-measurements',
    template: ` <app-measurements-view [measurements]="(measurements$ | async)!"></app-measurements-view> `,
    styles: [],
})
export class MeasurementsComponent implements OnInit{
    constructor(private readonly measurementService: MeasurementService) {
    }

    @Input() measurements$?: Observable<MeasurementDto[]>;

    ngOnInit(): void {
        this.measurements$ = this.measurementService.getMeasurements()
    }
}
