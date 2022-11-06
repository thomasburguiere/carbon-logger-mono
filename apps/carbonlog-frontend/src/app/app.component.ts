import {Component} from '@angular/core';

@Component({
    selector: 'crb-root',
    template: `
        <crb-measurements-list></crb-measurements-list>
        <crb-save-measurement></crb-save-measurement>
        `,
    styleUrls: ['./app.component.css']
})
export class AppComponent {

}
