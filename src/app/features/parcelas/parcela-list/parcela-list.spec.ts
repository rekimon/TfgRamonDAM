import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParcelaList } from './parcela-list';

describe('ParcelaList', () => {
  let component: ParcelaList;
  let fixture: ComponentFixture<ParcelaList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParcelaList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParcelaList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
