package com.enofex.naikan.overview.contact;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OverviewContactService {

  Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable);
}
