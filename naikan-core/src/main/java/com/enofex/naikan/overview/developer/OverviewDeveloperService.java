package com.enofex.naikan.overview.developer;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OverviewDeveloperService {

  Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable);
}
