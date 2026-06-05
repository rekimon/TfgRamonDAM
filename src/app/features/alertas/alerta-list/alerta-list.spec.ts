import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertaList } from './alerta-list';

describe('AlertaList', () => {
  let component: AlertaList;
  let fixture: ComponentFixture<AlertaList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertaList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlertaList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
