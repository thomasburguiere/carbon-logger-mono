import {Component, OnInit, Pipe, PipeTransform} from '@angular/core';
import {map, Observable} from 'rxjs';
import {MeasurementDto} from '../measurementService';
import {Store} from '@ngxs/store';
import {FetchMeasurements} from '../store/actions';
import {emptyTableInput, TableInput} from "@carbonlog/shared-ui";

@Component({
    selector: 'crb-measurements-list',
    template: `
        <h1 class="text-3xl font-bold underline">
            Measurements:
        </h1>
        <crb-table [tableInput]="measurements$ | async | toTableInput"></crb-table>
    `,
    styles: [],
})
export class MeasurementsListComponent implements OnInit {
    measurements$: Observable<MeasurementDto[]>;

    constructor(private store: Store) {
        this.measurements$ = this.store.select((state) => state.measurements.values);
    }

    ngOnInit(): void {
        this.store.dispatch(new FetchMeasurements());
    }
}

@Pipe({name: 'toTableInput'})
export class TableInputPipe implements PipeTransform {
    transform(measurements: MeasurementDto[] | null): TableInput {
        if (!measurements) {
            return emptyTableInput;
        }
        return {
            headers: ['CO2 Kgs', 'Date'],
            rows: measurements?.map(m => ({header: m.co2Kg, remainingCells: [m.dt]}))
        };
    }
}
