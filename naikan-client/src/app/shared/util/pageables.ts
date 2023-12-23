import {Injectable} from '@angular/core';
import {FilterMetadata} from 'primeng/api';
import {HttpParams} from '@angular/common/http';
import {TableLazyLoadEvent} from "primeng/table";

@Injectable()
export class Pageables {

  public static toAllPagesRequestHttpParams(event: TableLazyLoadEvent): HttpParams {
    let httpParams = Pageables.toPageRequestHttpParams(event);

    return httpParams.delete('page').delete('size');
  }

  public static toPageRequestHttpParams(event: TableLazyLoadEvent): HttpParams {
    if (event) {
      let params = new HttpParams();

      if (event.first !== undefined) {
        params = params.append('page', event.first === 0 ? 0 : (event.first / event.rows))
        params = params.append('size', `${event.rows}`);
      }

      if (event.globalFilter && !Array.isArray(event.globalFilter) && event.globalFilter.length > 0) {
        params = params.append('search', event.globalFilter);
      }

      if (event.multiSortMeta && event.multiSortMeta.length > 0) {
        event.multiSortMeta.forEach(item => {
          params = params.append('sort', item.field + ',' + (item.order === 1 ? 'asc' : 'desc'));
        });
      } else {
        if (event.sortField && event.sortField.length > 0) {
          params = params.append('sort', event.sortField + ',' + (event.sortOrder === 1 ? 'asc' : 'desc'));
        }
      }

      if (event.filters) {
        Object.keys(event.filters).forEach(key => {
          if (key !== 'global' && event.filters[key]) {
            if (Array.isArray(event.filters[key])) {
              const rules: FilterMetadata[] = <FilterMetadata[]>event.filters[key]

              rules.forEach(value => {
                params = this.rule({...value}, params, key);
              })
            } else {
              const rule: FilterMetadata = <FilterMetadata>event.filters[key]

              if (Array.isArray(rule.value)) {
                rule.value.forEach(value => {
                  const ruleCopy = {...rule};
                  ruleCopy.value = value;
                  params = this.rule(ruleCopy, params, key);
                })
              } else {
                params = this.rule(rule, params, key);
              }
            }
          }
        });
      }

      return params;
    }

    return new HttpParams();
  }

  private static rule(rule: FilterMetadata, params: HttpParams, key: string): HttpParams {
    if (rule.value) {
      let value = rule.value;

      if (rule.value instanceof Date) {
        value = rule.value.toISOString()
      }

      if (rule.operator) {
        params = params.append('filter', `${key},${value},${rule.matchMode},${rule.operator}`);
      } else {
        params = params.append('filter', `${key},${value},${rule.matchMode}`);
      }
    }
    return params;
  }
}