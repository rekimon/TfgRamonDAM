import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SensorHistorico } from './sensor-historico';

describe('SensorHistorico', () => {
  let component: SensorHistorico;
  let fixture: ComponentFixture<SensorHistorico>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SensorHistorico]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SensorHistorico);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
