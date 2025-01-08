import { Component } from '@angular/core';
import {CatService} from "../../../services/cat.service";
import {MatDialogRef} from "@angular/material/dialog";
import {AnimeService} from "../../../services/anime.service";

@Component({
  selector: 'app-new-anime',
  templateUrl: './new-anime.component.html',
  styleUrls: ['./new-anime.component.scss']
})
export class NewAnimeComponent {
  title: string = '';
  description: string = '';
  author: string = '';
  seasonNumber: number | null = null;
  season: string = 'SPRING';
  type: string = 'TV';
  releaseDate: string = '';
  status: string = 'AIRING';
  categoryNames: string = 'Select';
  paginatedclient: any = {size: 100, nb_data: 0, page: -1, data: [], pages: 1}
  constructor(private user_service: CatService,private service: AnimeService, private dialogRef: MatDialogRef<NewAnimeComponent>) {
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

  Add() {
    const data = {
      title: this.title,
      description: this.description,
      author: this.author,
      seasonNumber: this.seasonNumber,
      season: this.season,
      type: this.type,
      releaseDate: new Date(this.releaseDate).toISOString(),
      status: this.status,
      categoryNames: this.categoryNames.split(',').map(c => c.trim())
    };

    this.service.add_anime(data).then(() => {
      this.dialogRef.close();
    }).catch((err:any) => {
      if (err.status === 200) {
        this.dialogRef.close();
      }
    });
  }
}
