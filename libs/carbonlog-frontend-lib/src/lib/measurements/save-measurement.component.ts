import {Component} from '@angular/core';
import {Store} from "@ngxs/store";
import {SaveMeasurement} from "../store/actions";

@Component({
    selector: 'crb-save-measurement',
    template: `
        <crb-save
                [placeholder]="'2.3'"
                (valueChanged)="handleValueChanged($event)"
        ></crb-save>
    `,
    styles: []
})
export class SaveMeasurementComponent {

    constructor(private store: Store) {
    }
    handleValueChanged($event: string) {
        console.log('value changed', $event);
        this.store.dispatch(new SaveMeasurement(Number($event)));
    }

}
