import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {MeasurementsComponent, TableInputPipe} from './measurements/measurements.component';
import {NgxsModule} from "@ngxs/store";
import {MeasurementsState} from "./store/state";
import {MeasurementService} from "./measurementService";
import {NgxsReduxDevtoolsPluginModule} from '@ngxs/devtools-plugin';
import {TableComponent} from "./measurements/table.component";

@NgModule({
    imports: [
        HttpClientModule,
        CommonModule,
        NgxsReduxDevtoolsPluginModule.forRoot(),
        NgxsModule.forRoot([MeasurementsState]),
    ],
    declarations: [MeasurementsComponent, TableComponent, TableInputPipe],
    providers: [MeasurementService, MeasurementsState],
    exports: [MeasurementsComponent, TableComponent, TableInputPipe],
})
export class CarbonlogFrontendLibModule {
}
