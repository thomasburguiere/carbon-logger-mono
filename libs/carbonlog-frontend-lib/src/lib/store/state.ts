import {Action, State, StateContext} from "@ngxs/store";
import {Injectable} from "@angular/core";
import {FetchMeasurements} from "./actions";
import {tap} from "rxjs";
import {MeasurementDto, MeasurementService} from '../measurementService';

export interface MeasurementsStateModel {
    values: MeasurementDto[];
}

@State<MeasurementsStateModel>({
    name: 'measurements',
    defaults: {values: []}
})
@Injectable()
export class MeasurementsState {
    constructor(private measurementsService: MeasurementService) {
    }

    @Action(FetchMeasurements)
    fetchMeasurements(ctx: StateContext<MeasurementsStateModel>, action: FetchMeasurements) {
        return this.measurementsService.getMeasurements().pipe(
            tap((measurements: MeasurementDto[]) => {
                const state = ctx.getState();
                ctx.setState({
                    ...state,
                    values: measurements
                });
            })
        );
    }

}
