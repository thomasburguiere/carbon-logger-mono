import {TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {NxWelcomeComponent} from './nx-welcome.component';
import {RouterTestingModule} from '@angular/router/testing';
import {CarbonlogFrontendLibModule} from '@carbonlog/carbonlog-frontend-lib';

describe('AppComponent', () => {
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [
                CarbonlogFrontendLibModule, RouterTestingModule],
            declarations: [AppComponent, NxWelcomeComponent]
        }).compileComponents();
    });

    it('should create the app', () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.componentInstance;
        expect(app).toBeTruthy();
    });

    it(`should have as title 'carbonlog-frontend'`, () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.componentInstance;
        expect(app.title).toEqual('carbonlog-frontend');
    });

    xit('should render title', () => {
        const fixture = TestBed.createComponent(AppComponent);
        fixture.detectChanges();
        const compiled = fixture.nativeElement as HTMLElement;
        expect(compiled.querySelector('h1')?.textContent).toContain('Welcome carbonlog-frontend');
    });
});
