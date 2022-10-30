import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { MeasurementsComponent } from './measurements/measurements.component';
import { MeasurementsViewComponent } from './measurements/measurements.view.component';

@NgModule({
    imports: [HttpClientModule, CommonModule],
    declarations: [MeasurementsViewComponent, MeasurementsComponent],
    exports: [MeasurementsViewComponent, MeasurementsComponent],
})
export class CarbonlogFrontendLibModule {}
