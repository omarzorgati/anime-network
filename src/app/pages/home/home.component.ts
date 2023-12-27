import {Component, OnInit} from '@angular/core';
import {ArrayType} from "@angular/compiler";
import {HttpClient} from "@angular/common/http";

interface AnimeGallery {
  url: string;
  name: string;
  subTitle: string;
  gender: string[];
}

interface Anime {
  id: number;
  name: string;
  opened:bigint;
  description: string;
  image: string;
  genders: string[];
  comments: any[]; // You might want to replace "any" with the actual type of your comments
  favoritedBy: any[]; // You might want to replace "any" with the actual type of users
  ratings: any[]; // You might want to replace "any" with the actual type of ratings
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit{

  animeList: Anime[] = [];
  BestFive: Anime[] = [];

  ngOnInit() {
    this.getAnime();
    this.getBestFive();
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


  constructor(private http:HttpClient) {
  }

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

  showPrev() {
    this.currentIndex = (this.currentIndex - 1 + this.Gallery.length) % this.Gallery.length;
  }

  showNext() {
    this.currentIndex = (this.currentIndex + 1) % this.Gallery.length;
  }

}
