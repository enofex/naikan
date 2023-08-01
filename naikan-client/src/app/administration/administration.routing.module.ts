import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AdministrationComponent} from "./administration.component";
import {AuthGuard} from "@naikan/shared";

@NgModule({
    imports: [RouterModule.forChild([{
        path: '',
        component: AdministrationComponent,
        canActivate: [AuthGuard],
    }])],
    exports: [RouterModule]
})
export class AdministrationRoutingModule {
}
