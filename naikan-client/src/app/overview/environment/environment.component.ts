import {Component} from '@angular/core';
import {EnvironmentService} from './environment.service';
import {Breadcrumb, Charts, Developer, Search, Url} from '../../shared';
import {SharedModule} from 'primeng/api';
import {LayoutService} from '../../layout/app.layout.service';
import {AbstractOverviewComponent} from "../abstract-overview.component";
import {OverviewProjectTable} from '../overview-project-table';
import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';
import {ButtonModule} from 'primeng/button';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {ChartModule} from 'primeng/chart';
import {DatePipe, NgIf} from '@angular/common';
import {OverviewGroup} from "../overview";

@Component({
  templateUrl: './environment.component.html',
  standalone: true,
  imports: [
    Breadcrumb,
    NgIf,
    ChartModule,
    TableModule,
    SharedModule,
    Search,
    ButtonModule,
    TooltipModule,
    Url,
    TagModule,
    OverviewProjectTable,
  ],
  providers: [EnvironmentService, DatePipe]
})
export class EnvironmentComponent extends AbstractOverviewComponent<OverviewGroup<Developer>> {

  constructor(private readonly environmentService: EnvironmentService, layoutService: LayoutService) {
    super(layoutService);
  }

  loadOverviews(event?: TableLazyLoadEvent): void {
    this.environmentService.getOverviews(event)
    .subscribe(data => this.page = data);
  }

  override initChart(): void {
    this.environmentService.getTopGroups(this.topN)
    .subscribe(data => {
      if (data) {
        const documentStyle = Charts.documentStyle();

        this.chartData = {
          labels: data.names,
          datasets: [
            {
              label: "Projects",
              data: data.counts,
              backgroundColor: documentStyle,
              borderColor: documentStyle
            }
          ]
        };

        Object.assign(this.chartOptions.plugins.title, {'text': `Top ${this.topN} Environments`});
      }
    });
  }
}
