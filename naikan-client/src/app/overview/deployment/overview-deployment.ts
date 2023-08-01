import {OverviewBom} from "../overview";
import {Deployment} from "@naikan/shared";

export interface OverviewDeployment {
  bom: OverviewBom;
  deployment: Deployment;
}