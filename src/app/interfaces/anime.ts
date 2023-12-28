import {Users} from "./users";

export interface Anime {
  id: number;
  name: string;
  opened:bigint;
  description: string;
  image: string;
  genders: string[];
  comments: any[];
  favoritedBy: Users[];
  ratings: any[];
}
