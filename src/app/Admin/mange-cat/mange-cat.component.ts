import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";
import {GlobalVariables} from "../../global-variables";
import {CatService} from "../../services/cat.service";
import {UpdateCatComponent} from "./update-cat/update-cat.component";
import {NewCatComponent} from "./new-cat/new-cat.component";

@Component({
  selector: 'app-mange-cat',
  templateUrl: './mange-cat.component.html',
  styleUrls: ['./mange-cat.component.scss']
})
export class MangeCatComponent {
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

  // Elevate(email:any){
  //   this.user_service.elevate(email).then((res)=>{
  //
  //   }).catch((err)=>{
  //     if(err.status === 200){
  //       this.get_all(0)
  //     }
  //   })
  // }

  Update(data: any) {
    this.dialog.open(UpdateCatComponent, {
      data: data
    }).afterClosed().subscribe((res) => {
      this.get_all(0)
    })
  }

  add() {
    this.dialog.open(NewCatComponent, {}).afterClosed().subscribe((res) => {
      this.get_all(0)
    })
  }

  Delete(id: any) {
    this.user_service.delete_cat(id).then((res) => {
      this.get_all(0)
    }).catch((err) => {
      if (err.status === 200) {
        this.get_all(0)
      }
    })
  }

}
