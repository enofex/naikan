import {Deployment, Organization, Project} from "@naikan/shared";
import {DeploymentsPerMonth} from "./deployments-per-month";

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
  deploymentsPerMonth: DeploymentsPerMonth;
}