import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-animes',
  templateUrl: './animes.component.html',
  styleUrls: ['./animes.component.scss']
})
export class AnimesComponent implements OnInit{

  Genders : string[] = [];

  constructor(private http:HttpClient,private router:Router) {
  }

  ngOnInit() {
    this.Categories();
  }


  Categories(){
    this.http.get("http://localhost:8080/anime/all-genres").subscribe((res:any)=>{
      this.Genders = res;
      console.log(this.Genders);
    });
  }

  navigateToCategory(category: string) {
    this.router.navigate(['/categories', category]);
  }


}
