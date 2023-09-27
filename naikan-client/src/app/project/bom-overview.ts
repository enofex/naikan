import {Commit, Deployment, Organization, Project} from "@naikan/shared";
import {CountsPerItems} from "./counts-per-items";

export interface BomOverview {
  id: string;
  timestamp?: Date;
  project?: Project;
  organization?: Organization;
  environmentNames?: string[];
  teamNames?: string[];
  developerNames?: string[];
  contactNames?: string[];
  technologyNames?: string[];
  licenseNames?: string[];
  documentationNames?: string[];
  integrationNames?: string[];
  tags?: string[];
  deploymentsCount: number;
  deploymentsEnvironmentsCount: number;
  deploymentsVersionsCount: number;
  lastDeployment: Deployment;
  deploymentsPerMonth: CountsPerItems;
  commitsCount: number
  commitsPerMonth: CountsPerItems;
  repository: Repository;
}

export interface Repository {
  name?: string;
  url?: string;
  firstCommit?: Commit;
  totalCommits?: number;
  defaultBranch?: string;
}