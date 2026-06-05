import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TareaForm } from './tarea-form';

describe('TareaForm', () => {
  let component: TareaForm;
  let fixture: ComponentFixture<TareaForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TareaForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TareaForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
