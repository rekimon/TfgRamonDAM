import { TestBed } from '@angular/core/testing';

import { Parcela } from './parcela';

describe('Parcela', () => {
  let service: Parcela;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Parcela);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
