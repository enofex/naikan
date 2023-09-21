import {
  Contact,
  Deployment,
  Developer,
  Documentation,
  Environment,
  Integration,
  License,
  Organization,
  Project,
  Team,
  Technology
} from "@naikan/shared";
import {DeploymentsPerMonth} from "./deployments-per-month";

export interface BomOverview {
  id: string;
  timestamp?: Date;
  project?: Project;
  organization?: Organization;
  environments?: Environment[];
  teams?: Team[];
  developers?: Developer[];
  contacts?: Contact[];
  technologies?: Technology[];
  licenses?: License[];
  documentations?: Documentation[];
  integrations?: Integration[];
  tags?: string[];
  deploymentsCount: number;
  deploymentsEnvironmentsCount: number;
  deploymentsVersionsCount: number;
  lastDeployment: Deployment;
  deploymentsPerMonth: DeploymentsPerMonth;
}