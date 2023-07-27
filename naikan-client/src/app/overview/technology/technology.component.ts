import {Component} from '@angular/core';
import {LayoutService} from 'src/app/layout/app.layout.service';
import {TechnologyService} from './technology.service';
import {Breadcrumb, Charts, Search} from '../../shared';
import {SharedModule} from 'primeng/api';
import {AbstractOverviewComponent} from "../abstract-overview.component";
import {OverviewProjectTable} from '../overview-project-table';
import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';
import {ButtonModule} from 'primeng/button';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {ChartModule} from 'primeng/chart';
import {DatePipe, NgIf} from '@angular/common';
import {TechnologyGroup} from "./overview";

@Component({
  templateUrl: './technology.component.html',
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
    TagModule,
    OverviewProjectTable,
  ],
  providers: [TechnologyService, DatePipe]
})
export class TechnologyComponent extends AbstractOverviewComponent<TechnologyGroup> {

  constructor(private readonly technologyService: TechnologyService, layoutService: LayoutService) {
    super(layoutService);
  }

  loadOverviews(event?: TableLazyLoadEvent): void {
    this.technologyService.getOverviews(event)
    .subscribe(data => this.page = data);
  }

  override initChart(): void {
    this.technologyService.getTopGroups(this.topN)
    .subscribe(data => {
      if (data) {
        const documentStyle = Charts.documentStyle();
        this.topN = data.names?.length;

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

        Object.assign(this.chartOptions.plugins.title, {'text': `Top ${this.topN} Technologies`});
      }
    });
  }
}
