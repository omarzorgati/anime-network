import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  search:boolean = false;

  constructor() {
  }

  Search(){
    this.search = !this.search;
  }

}
