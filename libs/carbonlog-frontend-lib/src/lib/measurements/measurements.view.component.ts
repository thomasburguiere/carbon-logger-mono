import {Component, Input} from '@angular/core';
import {MeasurementDto} from '../measurementService';

@Component({
    selector: 'app-measurements-view',
    template: ` Measurements:<br>
    <ul>
    <li *ngFor="let measurement of measurements">{{measurement.co2Kg}} co2Kg - {{measurement.dt}}</li>
</ul>
  `,
    styles: [],
})
export class MeasurementsViewComponent {
    @Input() measurements?: MeasurementDto[];
}
