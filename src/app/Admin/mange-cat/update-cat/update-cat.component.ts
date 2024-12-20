import {Component, Inject} from '@angular/core';
import {CatService} from "../../../services/cat.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-update-cat',
  templateUrl: './update-cat.component.html',
  styleUrls: ['./update-cat.component.scss']
})
export class UpdateCatComponent {
  name:string=""


  constructor(@Inject(MAT_DIALOG_DATA) public data: any,private service:CatService,private dialogRef: MatDialogRef<UpdateCatComponent>,) {
    this.name = this.data.name;
  }

  Update(){
    const  data = {
      name:this.name
    }
    this.service.update_cat(data,this.data.id).then((res)=>{
      this.dialogRef.close()
    }).catch((err)=>{
      if(err.status === 200){
        this.dialogRef.close()
      }
    })
  }
}
