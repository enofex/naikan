import {Project} from '../shared';

export interface OverviewGroup {
  uuid: string;
  group: Group;
  boms?: OverviewBom[];
  count: number;
}

export interface Group {
  name: string;
}

export interface OverviewBom {
  id: string,
  timestamp?: Date,
  project?: Project
}