export interface Bom {
  id: string;
  specVersion: string;
  bomFormat: string;
  timestamp?: Date;
  project?: Project;
  organization?: Organization;
  environments?: Environment[];
  teams?: Team[];
  developers?: Developer[];
  contacts?: Contact[];
  technologies?: Technology[];
  licenses?: License[];
  documentations?: Documentation[];
  integrations?: Integration[];
  deployments?: Deployment[];
  tags?: string[];
}

export interface Project {
  name?: string;
  url?: string;
  repository?: string;
  packaging?: string;
  groupId?: string;
  artifactId?: string;
  version?: string;
  description?: string;
  notes?: string;
}

export interface Organization {
  name?: string;
  url?: string;
  department?: string;
  description?: string;
}

export interface Environment {
  name?: string;
  location?: string;
  description?: string;
  tags?: string[];
}

export interface Team {
  name?: string;
  description?: string;
}

export interface Developer {
  name?: string;
  username?: string;
  title?: string;
  department?: string;
  email?: string;
  phone?: string;
  organization?: string;
  organizationUrl?: string;
  timezone?: string;
  description?: string;
  roles?: string[];
}

export interface Contact {
  name?: string;
  title?: string;
  email?: string;
  phone?: string;
  description?: string;
  roles?: string[];
}

export interface Technology {
  name?: string;
  version?: string;
  description?: string;
  tags?: string[];
}

export interface Documentation {
  name?: string;
  location?: string;
  description?: string;
  tags?: string[];
}

export interface Integration {
  name?: string;
  url?: string;
  description?: string;
  tags?: string[];
}

export interface License {
  name?: string;
  url?: string;
  description?: string;
}

export interface Deployment {
  environment?: string;
  location?: string;
  version?: string;
  timestamp?: Date;
}
