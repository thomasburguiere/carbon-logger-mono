import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasurementsViewComponent } from './measurements.view.component';

describe('MeasurementsViewComponent', () => {
  let component: MeasurementsViewComponent;
  let fixture: ComponentFixture<MeasurementsViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MeasurementsViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MeasurementsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
