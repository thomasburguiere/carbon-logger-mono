import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {MeasurementsComponent} from './measurements/measurements.component';

@NgModule({
    imports: [HttpClientModule, CommonModule],
    declarations: [MeasurementsComponent],
    exports: [MeasurementsComponent],
})
export class CarbonlogFrontendLibModule {
}
