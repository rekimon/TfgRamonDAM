import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CultivoForm } from './cultivo-form';

describe('CultivoForm', () => {
  let component: CultivoForm;
  let fixture: ComponentFixture<CultivoForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CultivoForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CultivoForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
