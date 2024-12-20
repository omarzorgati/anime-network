import { Component } from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CatService} from "../../services/cat.service";
import {Router} from "@angular/router";
import {GlobalVariables} from "../../global-variables";
import {AnimeService} from "../../services/anime.service";
import {NewCatComponent} from "../mange-cat/new-cat/new-cat.component";
import {NewAnimeComponent} from "./new-anime/new-anime.component";

@Component({
  selector: 'app-mange-anime',
  templateUrl: './mange-anime.component.html',
  styleUrls: ['./mange-anime.component.scss']
})
export class MangeAnimeComponent {
  paginatedclient: any = {size: 20, nb_data: 0, page: -1, data: [], pages: 1}

  constructor(
    private dialog: MatDialog,
    private user_service: AnimeService,
    private router: Router,
    private glovar: GlobalVariables) {
    this.get_all(0)
  }

  get_all(page: any) {
    this.user_service.all_anime(this.paginatedclient.size, page).then((res) => {
      this.paginatedclient.nb_data = res.totalElements
      this.paginatedclient.page = res.number
      this.paginatedclient.pages = res.totalPages
      this.paginatedclient.data = res.content
    }).finally(() => {
    })
  }

  onClientPageChange(e: any) {
    this.get_all(e.page)
  }

  add() {
    this.dialog.open(NewAnimeComponent, {}).afterClosed().subscribe((res) => {
      this.get_all(0)
    })
  }
}
