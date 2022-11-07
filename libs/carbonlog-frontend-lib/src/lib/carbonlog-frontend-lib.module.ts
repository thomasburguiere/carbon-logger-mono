import {CommonModule} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import {NgModule} from "@angular/core";
import {MeasurementsListComponent, TableInputPipe} from "./measurements/measurements-list.component";
import {NgxsModule} from "@ngxs/store";
import {MeasurementsState} from "./store/state";
import {MeasurementService} from "./measurementService";
import {NgxsReduxDevtoolsPluginModule} from "@ngxs/devtools-plugin";
import {SharedUiModule} from "@carbonlog/shared-ui";
import {SaveMeasurementComponent} from "./measurements/save-measurement.component";

@NgModule({
    imports: [
        HttpClientModule,
        CommonModule,
        SharedUiModule,
        NgxsReduxDevtoolsPluginModule.forRoot(),
        NgxsModule.forRoot([MeasurementsState]),
    ],
    declarations: [MeasurementsListComponent, TableInputPipe, SaveMeasurementComponent],
    providers: [MeasurementService, MeasurementsState],
    exports: [MeasurementsListComponent, TableInputPipe, SaveMeasurementComponent],
})
export class CarbonlogFrontendLibModule {
}
