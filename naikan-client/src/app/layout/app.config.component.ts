import {Component} from '@angular/core';
import {LayoutService} from './app.layout.service';
import {FormsModule} from '@angular/forms';
import {RadioButtonModule} from 'primeng/radiobutton';
import {NgClass} from '@angular/common';
import {ButtonModule} from 'primeng/button';
import {DrawerModule} from 'primeng/drawer';

@Component({
    selector: 'app-config',
    templateUrl: './app.config.component.html',
    imports: [DrawerModule, ButtonModule, NgClass, RadioButtonModule, FormsModule]
})
export class AppConfigComponent {

    scales: number[] = [12, 13, 14, 15, 16];

    constructor(private readonly layoutService: LayoutService) {
    }

    get visible(): boolean {
        return this.layoutService.state.configSidebarVisible;
    }

    set visible(_val: boolean) {
        if (!_val) {
            this.layoutService.onConfigUpdate();
        }

        this.layoutService.state.configSidebarVisible = _val;
    }

    get scale(): number {
        return this.layoutService.config.scale;
    }

    set scale(_val: number) {
        this.layoutService.config.scale = _val;
    }

    get menuMode(): string {
        return this.layoutService.config.menuMode;
    }

    set menuMode(_val: string) {
        this.layoutService.config.menuMode = _val;
    }

    onConfigButtonClick(): void {
        this.layoutService.showConfigSidebar();
    }

    decrementScale(): void {
        this.scale--;
        this.applyScale();
    }

    incrementScale(): void {
        this.scale++;
        this.applyScale();
    }

    private applyScale(): void {
        document.documentElement.style.fontSize = this.scale + 'px';
    }
}
