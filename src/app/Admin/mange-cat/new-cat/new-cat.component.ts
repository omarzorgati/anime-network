import { Component } from '@angular/core';
import {CatService} from "../../../services/cat.service";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-new-cat',
  templateUrl: './new-cat.component.html',
  styleUrls: ['./new-cat.component.scss']
})
export class NewCatComponent {
  name:string=""


  constructor(private service:CatService,private dialogRef: MatDialogRef<NewCatComponent>,) {
  }

  Add(){
    const  data = {
      name:this.name
    }
    this.service.add_cat(data).then((res)=>{
      this.dialogRef.close()
    }).catch((err)=>{
      if(err.status === 200){
        this.dialogRef.close()
      }
    })
  }
}
