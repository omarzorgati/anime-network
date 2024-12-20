import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {CatService} from "../../services/cat.service";
import {GlobalVariables} from "../../global-variables";

@Component({
  selector: 'app-animes',
  templateUrl: './animes.component.html',
  styleUrls: ['./animes.component.scss']
})
export class AnimesComponent{

  paginatedclient: any = {size: 20, nb_data: 0, page: -1, data: [], pages: 1}

  constructor(
    private dialog: MatDialog,
    private user_service: CatService,
    private router: Router,
    private glovar: GlobalVariables) {
    this.get_all(0)
  }

  get_all(page: any) {
    this.user_service.all_cat(this.paginatedclient.size, page).then((res) => {
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

  navigateToCategory(category: any) {
    this.router.navigate(['/categories/'+category.name]);
  }


}
