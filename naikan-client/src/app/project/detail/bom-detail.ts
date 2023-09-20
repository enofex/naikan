import {
  Contact,
  Developer,
  Documentation,
  Environment, Integration,
  License,
  Organization,
  Project, Team,
  Technology
} from "@naikan/shared";

export interface BomDetail {
  id: string;
  specVersion: string;
  bomFormat: string;
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
}