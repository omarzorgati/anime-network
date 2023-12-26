import { Component } from '@angular/core';
import {ArrayType} from "@angular/compiler";

interface Anime {
  url: string;
  name: string;
  subTitle: string;
  gender: string[];
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  animeList: Anime[] = [
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
    this.currentIndex = (this.currentIndex - 1 + this.animeList.length) % this.animeList.length;
  }

  showNext() {
    this.currentIndex = (this.currentIndex + 1) % this.animeList.length;
  }

}
