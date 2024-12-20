import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MangeAnimeComponent } from './mange-anime.component';

describe('MangeAnimeComponent', () => {
  let component: MangeAnimeComponent;
  let fixture: ComponentFixture<MangeAnimeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MangeAnimeComponent]
    });
    fixture = TestBed.createComponent(MangeAnimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
