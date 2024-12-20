import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MangeReviewComponent } from './mange-review.component';

describe('MangeReviewComponent', () => {
  let component: MangeReviewComponent;
  let fixture: ComponentFixture<MangeReviewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MangeReviewComponent]
    });
    fixture = TestBed.createComponent(MangeReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
