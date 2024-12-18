import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {animate, state, style, transition, trigger} from "@angular/animations";

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
export class HeaderComponent implements OnInit{

  search:boolean = false;
  UserId:string = "";
  username:string = "";
  loading:boolean = false;
  UserMenu:boolean = false;

  ngOnInit(): void {
    this.UserId = localStorage.getItem('identification')!
    this.LoggedUser(parseInt(this.UserId));
  }

  LoggedUser(id:any){
    this.http.get<any>(`http://localhost:8080/users/${id}`).subscribe(
      (res) => {
        this.username = res.username;
      });
  }

  constructor(private route:Router,private http:HttpClient) {
  }

  Search(){
    this.search = !this.search;
  }


  ToggleUserMenu(){
    this.UserMenu = !this.UserMenu;
  }


  Signout(){
    this.loading = true
    localStorage.setItem("identification",'');
    setTimeout(()=>{
      this.loading = false;
      this.route.navigate(['/home']).then(()=>{
        window.location.reload();
      });
    },3000)

  }


  RedirectTo(location:string){
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
