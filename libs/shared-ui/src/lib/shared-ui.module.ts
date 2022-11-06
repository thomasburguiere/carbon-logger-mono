import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SaveComponent} from "./save.component";
import {TableComponent} from "./table.component";

@NgModule({
    imports: [
        CommonModule
    ],
    declarations: [SaveComponent, TableComponent],
    exports: [SaveComponent, TableComponent]
})
export class SharedUiModule {
}
