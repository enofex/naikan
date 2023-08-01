import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthGuard} from "@naikan/shared";
import {OverviewComponent} from "./overview.component";

@NgModule({
    imports: [RouterModule.forChild([{
        path: '',
        component: OverviewComponent,
        canActivate: [AuthGuard],
    }])],
    exports: [RouterModule]
})
export class OverviewRoutingModule {
}
