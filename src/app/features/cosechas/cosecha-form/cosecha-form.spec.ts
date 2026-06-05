import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CosechaForm } from './cosecha-form';

describe('CosechaForm', () => {
  let component: CosechaForm;
  let fixture: ComponentFixture<CosechaForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CosechaForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CosechaForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
