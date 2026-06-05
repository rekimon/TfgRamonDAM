import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParcelaForm } from './parcela-form';

describe('ParcelaForm', () => {
  let component: ParcelaForm;
  let fixture: ComponentFixture<ParcelaForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParcelaForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParcelaForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
