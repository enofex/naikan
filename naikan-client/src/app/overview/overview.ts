import {Project} from '../shared';

export interface OverviewGroup<T> {
  uuid: string;
  group: T;
  boms?: OverviewBom[];
  count: number;
}

export interface OverviewBom {
  id: string,
  timestamp?: Date,
  project?: Project
}