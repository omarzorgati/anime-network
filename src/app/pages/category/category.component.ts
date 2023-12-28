import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Anime} from "../../interfaces/anime";
import {HttpClient} from "@angular/common/http";
import {Users} from "../../interfaces/users";

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {

  selectedCategory!: string;


  added_to_fav:boolean = false;

  removed_from_fav:boolean = false;


  animeList: Anime[] = [];
  BestFive: Anime[] = [];

  UserId:number = -1;

  constructor(private route: ActivatedRoute,private router:Router,private http:HttpClient) { }

  ngOnInit() {
    this.GetCatName();
    this.getAnime();
    this.getBestFive();
    this.GetUserId();
  }

  GetCatName(){
    this.route.params.subscribe(params => {
      this.selectedCategory = params['category'];
    });
  }

  NavigateTo(location: string) {
    this.router.navigate([location]);
  }

  GetUserId(){
    this.UserId = parseInt(localStorage.getItem('identification')!);
  }

  getAnime() {
    this.http.get<Anime[]>(`http://localhost:8080/anime/by-genre/${this.selectedCategory}`).subscribe((res: Anime[]) => {
      this.animeList = res;
    });
  }

  getBestFive(){
    this.http.get<Anime[]>("http://localhost:8080/anime/top5").subscribe((res: Anime[]) => {
      this.BestFive = res;
    });
  }

  Heart(animeId: any) {
    if (localStorage.getItem('identification')) {
      const  UserId = parseInt(localStorage.getItem('identification')!);
      this.http.post(`http://localhost:8080/anime/${animeId}/favorite/${UserId}`,null).subscribe((res)=>{
        this.getAnime();
        this.fav_timer();
      })
    } else {
      this.router.navigate(['/login'])
    }
  }

  isUserFavorited(favoritedBy: Users[]): boolean {
    // @ts-ignore
    return favoritedBy.some(user => user.id === this.UserId);
  }


  fav_timer(){
    this.added_to_fav = true
    setTimeout(()=>{
      this.added_to_fav = false;
    },2000)
  }

  no_fav_timer(){
    this.removed_from_fav = true
    setTimeout(()=>{
      this.removed_from_fav = false;
    },2000)
  }

  Broke(animeId: any) {
    const  UserId = parseInt(localStorage.getItem('identification')!);
    this.http.delete(`http://localhost:8080/anime/${animeId}/favorite/${UserId}`).subscribe((res)=>{
      this.getAnime();
      this.no_fav_timer();
    })
  }


}
