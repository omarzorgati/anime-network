import { Component, HostListener } from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

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

  constructor(private route:Router,private http:HttpClient) {
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
        this.Loading = true
        setTimeout(()=>{
          this.CheckCrendials();
          this.Loading = false;
          setTimeout(()=>{
            this.RedirctTo('/home');
          },2000)
        },3000)
    }

  }


  CheckCrendials(){
    const Data={
      email: this.email,
      password: this.password
    }
    this.http.post<any>("http://localhost:8080/users/authenticate", Data).subscribe(
      (res) => {
        // this.UserId = res.id;
        localStorage.setItem('identification',res.id);
        localStorage.setItem('username',res.username);
        this.msg = "You have been logged in successfully";
        this.clear();
        setTimeout(()=>{
          this.msg = ""
        },2000)
      },
      (error) => {
        console.error('Error:', error);
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
