import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  animations: [
    trigger('slideInOut', [
      state('in', style({
        transform: 'translateY(0)',
        opacity: 1,
      })),
      transition('void => *', [
        style({
          transform: 'translateY(-100%)',
          opacity: 0,
        }),
        animate('300ms ease-out'),
      ]),
      transition('* => void', [
        animate('300ms ease-out', style({
          transform: 'translateY(-100%)',
          opacity: 0,
        })),
      ]),
    ]),
  ],
})
export class HeaderComponent implements OnInit {

  search: boolean = false;
  UserId: string = "";
  username: string = "";
  loading: boolean = false;
  UserMenu: boolean = false;
  CurrentUser: any;

  ngOnInit(): void {

  }


  constructor(private route: Router, private Auth: AuthService) {
    this.Init_User()
  }


  Init_User(){
    this.Auth.CurrentUser().then((res) => {
      this.CurrentUser = res;
      console.log(res)
    }).catch((err)=>{})
  }
  Search() {
    this.search = !this.search;
  }


  ToggleUserMenu() {
    this.UserMenu = !this.UserMenu;
  }


  Signout() {
    this.UserMenu  = false
    this.Auth.Logout();
    this.route.navigate(['/login']).then(() => {
    });

  }


  RedirectTo(location: string) {
    this.route.navigate([location]);
    this.UserMenu = false;
  }

  isRoute(location: string): boolean {

    const currentRoute = this.route.url;

    return currentRoute === location;
  }

  hasAdminInRoute(): boolean {
    const currentRoute = this.route.url.toLowerCase(); // Ensure case-insensitivity
    return currentRoute.includes('admin');
  }


}
