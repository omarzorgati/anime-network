import { Component } from '@angular/core';
import {GlobalVariables} from "../../global-variables";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-manage-user',
  templateUrl: './manage-user.component.html',
  styleUrls: ['./manage-user.component.scss']
})
export class ManageUserComponent {
  paginatedclient: any = {size:20,nb_data:0,page:-1,data:[],pages:1}

  constructor(
    private dialog : MatDialog,
    private user_service:UserService,
    private router:Router,
    private glovar : GlobalVariables) {
    this.get_all(0)
  }

  get_all(page:any){
    this.user_service.all_users(this.paginatedclient.size,page).then((res)=>{
      this.paginatedclient.nb_data = res.totalElements
      this.paginatedclient.page = res.number
      this.paginatedclient.pages=res.totalPages
      this.paginatedclient.data=res.content
    }).finally(()=>{
    })
  }
  onClientPageChange(e:any){
    this.get_all(e.page)
  }

  Elevate(email:any){
    this.user_service.elevate(email).then((res)=>{

    }).catch((err)=>{
      if(err.status === 200){
        this.get_all(0)
      }
    })
  }

  Delete(id:any){
    this.user_service.delete(id).then((res)=>{

    }).catch((err)=>{
      if(err.status === 200){
        this.get_all(0)
      }
    })
  }


}
