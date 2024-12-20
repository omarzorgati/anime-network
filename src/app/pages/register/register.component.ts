import {Component, HostListener} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import { trigger, state, style, transition, animate } from '@angular/animations';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
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
export class RegisterComponent {
  showBackToTop: boolean = false;
  msg:string = "";

  //user
  firstname:string=""
  lastname:string=""
  email:string=""
  password:string=""
  re_password:string=""


  Loading:boolean = false;


  constructor(private route:Router,private AuthService:AuthService) {
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



  Register() {
    if (this.firstname === '' || this.lastname === '' || this.email === '' || this.password === '' || this.re_password === '') {
      this.msg = "You cannot create an account with no information provided";
      setTimeout(() => {
        this.msg = "";
      }, 2000);
    } else {
      if (this.password !== this.re_password) {
        this.msg = "No password match has been found";
        setTimeout(() => {
          this.msg = "";
        }, 2000);
      } else {
        // Password validation: Length and at least one uppercase letter, one number
        const passwordRegex = /^(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{6,20}$/;
        if (!passwordRegex.test(this.password)) {
          this.msg = "Password must be between 6 to 20 characters and include at least one uppercase letter and one number.";
          setTimeout(() => {
            this.msg = "";
          }, 2000);
        } else {
          this.Loading = true;
          setTimeout(() => {
            this.CreateUser();
            this.Loading = false;
          }, 3000);
        }
      }
    }
  }


  clear(){
    this.firstname = ""
    this.lastname = ""
    this.password = ""
    this.email = ""
    this.re_password = ""
  }


  CreateUser(){
    const Data = {
      firstname:this.firstname,
      lastname:this.lastname,
      password:this.password,
      repeatPassword:this.re_password,
      email:this.email
    }

    this.AuthService.Register(Data).then((res)=>{
    }).catch((err)=>{
      if(err.status=== 200){
        this.msg = "You have been created your account with success, Please Check your email";
        this.clear();
        setTimeout(()=>{
          this.msg = ""
        },2000)
      }
    })
  }

  RedirctTo(d:string){
    this.route.navigate([d]);
  }

}
