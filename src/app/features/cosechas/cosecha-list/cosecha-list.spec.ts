import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CosechaList } from './cosecha-list';

describe('CosechaList', () => {
  let component: CosechaList;
  let fixture: ComponentFixture<CosechaList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CosechaList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CosechaList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
