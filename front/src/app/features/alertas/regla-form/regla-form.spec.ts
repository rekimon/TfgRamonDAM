import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReglaForm } from './regla-form';

describe('ReglaForm', () => {
  let component: ReglaForm;
  let fixture: ComponentFixture<ReglaForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReglaForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReglaForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
