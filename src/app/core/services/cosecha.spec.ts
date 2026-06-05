import { TestBed } from '@angular/core/testing';

import { Cosecha } from './cosecha';

describe('Cosecha', () => {
  let service: Cosecha;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Cosecha);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
