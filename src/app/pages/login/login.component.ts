import { Component, HostListener } from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";
import {CookieService} from "ngx-cookie-service";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  showBackToTop: boolean = false;
  msg:string = "";
  email:string = ""
  password:string = ""
  UserId:number = -1;
  Loading:boolean = false;

  constructor(private route:Router,private http:HttpClient,private service:AuthService,private cookieService:CookieService) {
  }



  @HostListener('window:scroll', ['$event'])
  onWindowScroll() {
    // Check the scroll position
    const scrollPosition = window.scrollY || document.documentElement.scrollTop || document.body.scrollTop || 0;

    // Show/hide the back-top button based on the scroll position
    this.showBackToTop = scrollPosition > 100; // You can adjust this threshold as needed
  }

  scrollToTop() {
    // Scroll back to the top
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }


  ForgetPassword(){

  }

  Login(){
    if(this.email === '' || this.password === ''){
      this.msg = "Email or password was not found ! please check"
      setTimeout(()=>{
        this.msg = ""
      },2000)
    }else{
      this.CheckCrendials();
    }

  }


  CheckCrendials(){
    this.Loading = true
    const Data={
      email: this.email,
      password: this.password
    }
    this.service.SignIn(Data).then(
      (res) => {
        this.Loading = false
        const today = new Date()
        const tomorrow = new Date(today)
        tomorrow.setDate(tomorrow.getDate() + 1)
        this.cookieService.set('TOKEN_DASH_ANIME', res.token!, tomorrow, '/');
        this.cookieService.set('STATE_DASH_ANIME', 'true', tomorrow, '/');
        this.cookieService.set('ROLE_DASH_ANIME', res.name, tomorrow, '/');
        if(res.name === 'USER'){
          this.route.navigate(['/home']);
        }else{
          this.route.navigate(['/Admin/Dashboard']);
        }

        this.msg = "You have been logged in successfully";
        this.clear();
        setTimeout(()=>{
          this.msg = ""
        },2000)
      },
      (error) => {
      }
    );
  }

  clear(){
    this.email = ""
    this.password  = ""
  }

  RedirctTo(d:string){
    this.route.navigate([d]);
  }


}
