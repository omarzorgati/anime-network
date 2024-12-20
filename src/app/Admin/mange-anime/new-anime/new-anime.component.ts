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
  categoryNames: string = '';

  constructor(private service: AnimeService, private dialogRef: MatDialogRef<NewAnimeComponent>) {}

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
