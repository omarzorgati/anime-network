import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MatDialog} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {Anime} from "../../interfaces/anime";
import {AnimeGallery} from "../../interfaces/anime-gallery";
import {Users} from "../../interfaces/users";
import {animate, state, style, transition, trigger} from "@angular/animations";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
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
export class HomeComponent implements OnInit{

  animeList: Anime[] = [];
  BestFive: Anime[] = [];


  added_to_fav:boolean = false;

  removed_from_fav:boolean = false;
  UserId:number = -1;

  Gallery: AnimeGallery[] = [
    {
      url: 'https://s.yimg.com/ny/api/res/1.2/LKeXpfVTBDB5YaWjtYBlWg--/YXBwaWQ9aGlnaGxhbmRlcjt3PTY0MDtoPTM2MA--/https://media.zenfs.com/en/comingsoon_net_477/5e257a80196052d3e53e2cac481eaed9',
      name: 'One Piece',
      subTitle: 'The adventures of Monkey D.Luffy and his crew, ...',
      gender: ['Adventure','Science','fantasy']
    },
    {
      url: 'https://images6.alphacoders.com/820/820024.png',
      name: 'boku no hero academia',
      subTitle: 'A superhero-admiring boy enrolls in a prestigious hero academy and learns ....',
      gender: ['Adventure','Science','fantasy','Superhero']
    },
    {
      url: 'https://wallpapers.com/images/featured/seven-deadly-sins-nhhfddtmqub0hqsj.jpg',
      name: 'The seven deadly sins',
      subTitle: 'The seven deadly sins, also known as the capital vices or cardinal sins ...',
      gender: ['Fantasy','Adventure']
    }
  ];

  currentIndex = 0;

  constructor(private http:HttpClient,private modal:MatDialog,private router:Router) {
  }

  ngOnInit() {
    this.getAnime();
    this.getBestFive();
    this.GetUserId();
  }


  GetUserId(){
    this.UserId = parseInt(localStorage.getItem('identification')!);
  }

  getAnime() {
    this.http.get<Anime[]>("http://localhost:8080/anime/all").subscribe((res: Anime[]) => {
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

  Broke(animeId: any) {
    const  UserId = parseInt(localStorage.getItem('identification')!);
    this.http.delete(`http://localhost:8080/anime/${animeId}/favorite/${UserId}`).subscribe((res)=>{
      this.getAnime();
      this.no_fav_timer();
    })
  }

  isUserFavorited(favoritedBy: Users[]): boolean {
    // @ts-ignore
    return favoritedBy.some(user => user.id === this.UserId);
  }

  showPrev() {
    this.currentIndex = (this.currentIndex - 1 + this.Gallery.length) % this.Gallery.length;
  }

  showNext() {
    this.currentIndex = (this.currentIndex + 1) % this.Gallery.length;
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








}
