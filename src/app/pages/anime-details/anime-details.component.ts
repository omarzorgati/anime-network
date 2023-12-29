import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {Anime} from "../../interfaces/anime";
import {Users} from "../../interfaces/users";
import {JapaneseTextGenerator} from "../../entities/japanese-text-generator";
import {Review} from "../../interfaces/review";

@Component({
  selector: 'app-anime-details',
  templateUrl: './anime-details.component.html',
  styleUrls: ['./anime-details.component.scss']
})
export class AnimeDetailsComponent implements OnInit{

  selectedAnime!:Anime;
  AnimeId!:number;
  JapaneseText!:string;
  BestFive: Anime[] = [];

  Comment:string = "";

  UserId:number = -1;

  username:string = "";

  reviews: Review[] = [];


  constructor(private router:Router,private http:HttpClient,private route: ActivatedRoute) {
    this.GetAnimeId();
    this.GetUserId();
    this.LoadUsername();
  }
  ngOnInit() {
    this.GetAnimeById();
    this.Genreate();
    this.getBestFive();
    this.GetCommentsByAnimeId(this.AnimeId);

  }

  getBestFive(){
    this.http.get<Anime[]>("http://localhost:8080/anime/top5").subscribe((res: Anime[]) => {
      this.BestFive = res;
    });
  }

  LoadUsername(){
    this.username = localStorage.getItem("username")!;
  }

  GetUserId(){
    this.UserId = parseInt(localStorage.getItem('identification')!);
  }

  GetAnimeId(){
    this.AnimeId = parseInt(this.route.snapshot.paramMap.get('id')!);
  }

  NavigateTo(location: string) {
    this.router.navigate([location]);
  }


  GetAnimeById(){
    this.http.get(`http://localhost:8080/anime/${this.AnimeId}`).subscribe((res:any)=>{
      this.selectedAnime = res;
    })
  }


  isUserFavorited(favoritedBy: Users[]): boolean {
    // @ts-ignore
    return favoritedBy.some(user => user.id === this.UserId);
  }


  Heart(animeId: any) {
    if (localStorage.getItem('identification')) {
      const  UserId = parseInt(localStorage.getItem('identification')!);
      this.http.post(`http://localhost:8080/anime/${animeId}/favorite/${UserId}`,null).subscribe((res)=>{
        this.GetAnimeById();
      })
    } else {
      this.router.navigate(['/login'])
    }
  }

  Broke(animeId: any) {
    const  UserId = parseInt(localStorage.getItem('identification')!);
    this.http.delete(`http://localhost:8080/anime/${animeId}/favorite/${UserId}`).subscribe((res)=>{
      this.GetAnimeById();
    })
  }

  Genreate():string{
    const textGenerator = new JapaneseTextGenerator();
    return this.JapaneseText = textGenerator.generateRandomJapaneseText(10);
  }


  PostComment(animeId: any) {
    if (this.Comment.trim() !== "") {
      const commentData = {
        animeId: animeId ,
        userId: this.UserId ,
        username: this.username,
        comment: this.Comment
      };

      this.http.post("http://localhost:8080/reviews/create", commentData).subscribe(
        (res) => {
          this.Clear()
          this.GetCommentsByAnimeId(this.AnimeId);
        },
        (error) => {
          console.error("Error posting comment", error);
        }
      );
    }
  }

  GetCommentsByAnimeId(animeId:any){
    this.http.get(`http://localhost:8080/reviews/anime/${animeId}`).subscribe(
      (res:any) => {
        this.reviews = res;
      },
      (error) => {
        console.error("Error posting comment", error);
      }
    );
  }

  Clear(){
    this.Comment = ""
  }


  getFirstLetter(word: string): string {
    if (word && word.length > 0) {
      return word.charAt(0).toUpperCase();
    } else {
      return '';
    }
  }




}
