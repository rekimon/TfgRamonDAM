import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsuarioDetail } from './usuario-detail';

describe('UsuarioDetail', () => {
  let component: UsuarioDetail;
  let fixture: ComponentFixture<UsuarioDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsuarioDetail]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsuarioDetail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
