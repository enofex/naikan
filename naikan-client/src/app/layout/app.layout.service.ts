import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';

export interface AppConfig {
  inputStyle: string;
  colorScheme: string;
  theme: string;
  menuMode: string;
  scale: number;
}

interface LayoutState {
  staticMenuDesktopInactive: boolean;
  overlayMenuActive: boolean;
  profileSidebarVisible: boolean;
  configSidebarVisible: boolean;
  staticMenuMobileActive: boolean;
  menuHoverActive: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class LayoutService {

  config: AppConfig = {
    inputStyle: 'filled',
    menuMode: 'static',
    colorScheme: 'light',
    theme: 'naikan',
    scale: 13,
  };

  state: LayoutState = {
    staticMenuDesktopInactive: false,
    overlayMenuActive: false,
    profileSidebarVisible: false,
    configSidebarVisible: false,
    staticMenuMobileActive: false,
    menuHoverActive: false
  };

  private configUpdate = new Subject<AppConfig>();
  private overlayOpen = new Subject<any>();

  configUpdate$ = this.configUpdate.asObservable();

  overlayOpen$ = this.overlayOpen.asObservable();

  constructor() {
    const sessionConfig = JSON.parse(sessionStorage.getItem("naikan-config"));

    if (sessionConfig) {
      this.config = sessionConfig;

      document.documentElement.style.fontSize = this.config.scale + 'px';
    }
  }

  onMenuToggle(): void {
    if (this.isOverlay()) {
      this.state.overlayMenuActive = !this.state.overlayMenuActive;
      if (this.state.overlayMenuActive) {
        this.overlayOpen.next(null);
      }
    }

    if (this.isDesktop()) {
      this.state.staticMenuDesktopInactive = !this.state.staticMenuDesktopInactive;
    } else {
      this.state.staticMenuMobileActive = !this.state.staticMenuMobileActive;

      if (this.state.staticMenuMobileActive) {
        this.overlayOpen.next(null);
      }
    }
  }

  showProfileSidebar(): void {
    this.state.profileSidebarVisible = !this.state.profileSidebarVisible;
    if (this.state.profileSidebarVisible) {
      this.overlayOpen.next(null);
    }
  }

  showConfigSidebar(): void {
    this.state.configSidebarVisible = true;
  }

  isOverlay(): boolean {
    return this.config.menuMode === 'overlay';
  }

  isDesktop(): boolean {
    return window.innerWidth > 991;
  }

  onConfigUpdate(): void {
    sessionStorage.setItem("naikan-config", JSON.stringify(this.config));
    this.configUpdate.next(this.config);
  }
}
