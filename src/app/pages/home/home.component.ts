import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MatDialog} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {Anime} from "../../interfaces/anime";
import {AnimeGallery} from "../../interfaces/anime-gallery";
import {Users} from "../../interfaces/users";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {AnimeService} from "../../services/anime.service";
import {GlobalVariables} from "../../global-variables";


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
  paginatedclient: any = {size: 20, nb_data: 0, page: -1, data: [], pages: 1}
  MostLikedAnime: any = {size: 20, nb_data: 0, page: -1, data: [], pages: 1}

  animeList: Anime[] = [];
  BestFive: Anime[] = [];

  model:boolean = false;

  added_to_fav:boolean = false;

  removed_from_fav:boolean = false;
  UserId:number = -1;

  Selected_Status:string = "";

  AnimeId:number  = -1;

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

  constructor(private glovar:GlobalVariables,private animeService:AnimeService,private modal:MatDialog,private router:Router) {
  }

  ngOnInit() {
    this.getAnime(0);
    this.MostLiked(0);
    this.GetUserId();
  }


  GetUserId(){
    this.UserId = parseInt(localStorage.getItem('identification')!);
  }

  getAnime(page:any): void {
    this.animeService.all_anime(
      this.paginatedclient.size, page,
      null,  // animeTitle (optional filter)
      null,  // season (optional filter)
      null,  // status (optional filter)
      null,  // animeType (optional filter)
      []     // categories (optional filter)
    )
      .then((res) => {
        this.paginatedclient.nb_data = res.totalElements
        this.paginatedclient.page = res.number
        this.paginatedclient.pages = res.totalPages
        this.paginatedclient.data = res.content
      })
      .catch((error) => {
        console.error('Error fetching anime:', error);
      });
  }

  MostLiked(page:any): void {
    this.animeService.most_liked(
      this.MostLikedAnime.size, page
    )
      .then((res) => {
        this.MostLikedAnime.nb_data = res.totalElements
        this.MostLikedAnime.page = res.number
        this.MostLikedAnime.pages = res.totalPages
        this.MostLikedAnime.data = res.content
      })
      .catch((error) => {
        console.error('Error fetching anime:', error);
      });
  }

  Like_Anime(id:any){
    this.animeService.like_anime(id).then((res)=>{
      this.glovar.showMsg("You have liked this anime")
    }).catch((err)=>{
    })
  }




  showPrev() {
    this.currentIndex = (this.currentIndex - 1 + this.Gallery.length) % this.Gallery.length;
  }

  showNext() {
    this.currentIndex = (this.currentIndex + 1) % this.Gallery.length;
  }

  AnimeDetails(id:number){
    this.router.navigate(['/anime', id]);
  }



  CloseModel(){
    this.model = false;
    this.AnimeId = -1;
  }
}
