import {OverviewBom} from "../overview";
import {Deployment} from "../../shared";

export interface OverviewDeployment {
  bom: OverviewBom;
  deployment: Deployment;
}