import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-dash',
  templateUrl: './admin-dash.component.html',
  styleUrls: ['./admin-dash.component.scss']
})
export class AdminDashComponent {
  navItems = [
    {
      label: 'User Management',
      icon: 'fa-solid fa-users', // Font Awesome class for users icon
      route: '/Admin/Manage/User'
    },
    {
      label: 'Review Management',
      icon: 'fa-solid fa-star', // Font Awesome class for review icon
      route: '/Admin/Manage/Review'
    },
    {
      label: 'Category Management',
      icon: 'fa-solid fa-list', // Font Awesome class for categories icon
      route: '/Admin/Manage/Category'
    },
    {
      label: 'Anime Management',
      icon: 'fa-solid fa-film', // Font Awesome class for anime/movies icon
      route: '/Admin/Manage/Anime'
    }
  ];
}
