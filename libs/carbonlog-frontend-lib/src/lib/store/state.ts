import {Action, State, StateContext} from "@ngxs/store";
import {Injectable} from "@angular/core";
import {FetchMeasurements, SaveMeasurement} from "./actions";
import {mergeMap, tap} from "rxjs";
import {MeasurementDto, MeasurementService} from "../measurementService";

export interface MeasurementsStateModel {
    values: MeasurementDto[];
}

@State<MeasurementsStateModel>({
    name: "measurements",
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

    @Action(SaveMeasurement)
    saveMeasurement(ctx: StateContext<MeasurementsStateModel>, action: SaveMeasurement) {
        return this.measurementsService.saveMeasurementCo2Kg(action.co2Kg)
            .pipe(
                mergeMap(() => this.measurementsService.getMeasurements()),
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
