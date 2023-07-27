import {OverviewBom} from "../overview";

export interface TechnologyGroup {
  uuid: string;
  group: Group;
  boms?: OverviewBom[];
  count: number;
}

export interface Group {
  name: string;
  version: string;
}
