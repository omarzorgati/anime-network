import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from './token.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const service = inject(TokenService);
  const router = inject(Router);

  const isLoggedIn = service.isLoggedIn();
  const hasRole = service.getRole();


  if (!hasRole) {
    router.navigate(['/login']);
    return false;
  }


  // If the user is not logged in, redirect to login page
  if (!isLoggedIn) {
    router.navigate(['/login']);
    return false;
  }

  // If the user is ROLE_ADMIN, allow access to all routes
  if (hasRole === 'ADMIN') {
    return true;
  }

  // If the user is ROLE_USER, restrict access to routes containing "admin"
  if (hasRole === 'USER' && !state.url.includes('admin')) {
    return true;
  }

  // If the user is ROLE_USER and tries to access an "admin" route, redirect to login
  router.navigate(['/login']);
  return false;
};
