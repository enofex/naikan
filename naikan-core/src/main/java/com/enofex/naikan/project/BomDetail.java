package com.enofex.naikan.project;

import com.enofex.naikan.model.Contacts;
import com.enofex.naikan.model.Developers;
import com.enofex.naikan.model.Documentations;
import com.enofex.naikan.model.Environments;
import com.enofex.naikan.model.Integrations;
import com.enofex.naikan.model.Licenses;
import com.enofex.naikan.model.Organization;
import com.enofex.naikan.model.Project;
import com.enofex.naikan.model.Repository;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Teams;
import com.enofex.naikan.model.Technologies;
import java.time.LocalDateTime;

public record BomDetail(String id, String bomFormat, String specVersion, LocalDateTime timestamp,
                        Project project, Organization organization, Environments environments,
                        Teams teams, Developers developers, Contacts contacts,
                        Technologies technologies, Licenses licenses,
                        Documentations documentations, Integrations integrations, Tags tags,
                        Repository repository) {

}
