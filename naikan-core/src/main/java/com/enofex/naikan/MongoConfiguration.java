package com.enofex.naikan;

import com.enofex.naikan.model.AbstractContainer;
import com.enofex.naikan.model.Branch;
import com.enofex.naikan.model.Branches;
import com.enofex.naikan.model.Commit;
import com.enofex.naikan.model.CommitAuthor;
import com.enofex.naikan.model.CommitChanges;
import com.enofex.naikan.model.CommitFilesChanges;
import com.enofex.naikan.model.CommitLinesChanges;
import com.enofex.naikan.model.Commits;
import com.enofex.naikan.model.Contact;
import com.enofex.naikan.model.Contacts;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.Deployments;
import com.enofex.naikan.model.Developer;
import com.enofex.naikan.model.Developers;
import com.enofex.naikan.model.Documentation;
import com.enofex.naikan.model.Documentations;
import com.enofex.naikan.model.Environment;
import com.enofex.naikan.model.Environments;
import com.enofex.naikan.model.Integration;
import com.enofex.naikan.model.Integrations;
import com.enofex.naikan.model.License;
import com.enofex.naikan.model.Licenses;
import com.enofex.naikan.model.RepositoryTag;
import com.enofex.naikan.model.RepositoryTags;
import com.enofex.naikan.model.Roles;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Team;
import com.enofex.naikan.model.Teams;
import com.enofex.naikan.model.Technologies;
import com.enofex.naikan.model.Technology;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.Jsr310Converters.DateToLocalDateTimeConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration(proxyBeanMethods = false)
public class MongoConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "naikan", name = "mongodb.transaction.enable", havingValue = "true", matchIfMissing = true)
  MongoTransactionManager transactionManager(MongoDatabaseFactory databaseFactory) {
    return new MongoTransactionManager(databaseFactory);
  }

  @Bean
  MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory,
      MappingMongoConverter converter) {
    converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    return new MongoTemplate(databaseFactory, converter);
  }

  @Bean
  MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(List.of(
        AbstractContainerWritingConverter.INSTANCE,
        ToEnvironmentsReadingConverter.INSTANCE,
        ToTeamsReadingConverter.INSTANCE,
        ToDevelopersReadingConverter.INSTANCE,
        ToContactsReadingConverter.INSTANCE,
        ToTechnologiesReadingConverter.INSTANCE,
        ToLicencesReadingConverter.INSTANCE,
        ToDocumentationsReadingConverter.INSTANCE,
        ToIntegrationsReadingConverter.INSTANCE,
        ToTagsReadingConverter.INSTANCE,
        ToRolesReadingConverter.INSTANCE,
        ToDeploymentsReadingConverter.INSTANCE,
        ToBranchesReadingConverter.INSTANCE,
        ToRepositoryTagsReadingConverter.INSTANCE,
        ToCommitsReadingConverter.INSTANCE
    ));
  }

  @WritingConverter
  private enum AbstractContainerWritingConverter implements
      Converter<AbstractContainer, List<Object>> {

    INSTANCE;

    @Override
    public List<Object> convert(AbstractContainer source) {
      return source.all();
    }
  }

  @ReadingConverter
  private enum ToEnvironmentsReadingConverter implements
      Converter<List<Document>, Environments> {

    INSTANCE;

    @Override
    public Environments convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<Environment> environments = new ArrayList<>(source.size());

      for (Document element : source) {
        environments.add(new Environment(
            element.getString("name"),
            element.getString("location"),
            element.getString("description"),
            ToTagsReadingConverter.INSTANCE.convert(element.getList("tags", String.class))
        ));
      }

      return new Environments(environments);
    }
  }

  @ReadingConverter
  private enum ToTeamsReadingConverter implements Converter<List<Document>, Teams> {

    INSTANCE;

    @Override
    public Teams convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<Team> teams = new ArrayList<>(source.size());

      for (Document element : source) {
        teams.add(new Team(
            element.getString("name"),
            element.getString("description")
        ));
      }

      return new Teams(teams);
    }
  }

  @ReadingConverter
  private enum ToDevelopersReadingConverter implements Converter<List<Document>, Developers> {

    INSTANCE;

    @Override
    public Developers convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<Developer> developers = new ArrayList<>(source.size());

      for (Document element : source) {
        developers.add(new Developer(
            element.getString("name"),
            element.getString("username"),
            element.getString("title"),
            element.getString("department"),
            element.getString("email"),
            element.getString("phone"),
            element.getString("organization"),
            element.getString("organizationUrl"),
            element.getString("timezone"),
            element.getString("description"),
            ToRolesReadingConverter.INSTANCE.convert(element.getList("roles", String.class))
        ));
      }

      return new Developers(developers);
    }
  }

  @ReadingConverter
  private enum ToContactsReadingConverter implements Converter<List<Document>, Contacts> {

    INSTANCE;

    @Override
    public Contacts convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<Contact> contacts = new ArrayList<>(source.size());

      for (Document element : source) {
        contacts.add(new Contact(
            element.getString("name"),
            element.getString("title"),
            element.getString("email"),
            element.getString("phone"),
            element.getString("description"),
            ToRolesReadingConverter.INSTANCE.convert(element.getList("roles", String.class))
        ));
      }

      return new Contacts(contacts);
    }
  }

  @ReadingConverter
  private enum ToTechnologiesReadingConverter implements Converter<List<Document>, Technologies> {

    INSTANCE;

    @Override
    public Technologies convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<Technology> technologies = new ArrayList<>(source.size());

      for (Document element : source) {
        technologies.add(new Technology(
            element.getString("name"),
            element.getString("version"),
            element.getString("description"),
            ToTagsReadingConverter.INSTANCE.convert(element.getList("tags", String.class))
        ));
      }

      return new Technologies(technologies);
    }
  }

  @ReadingConverter
  private enum ToLicencesReadingConverter implements Converter<List<Document>, Licenses> {

    INSTANCE;

    @Override
    public Licenses convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<License> licenses = new ArrayList<>(source.size());

      for (Document element : source) {
        licenses.add(new License(
            element.getString("name"),
            element.getString("url"),
            element.getString("description")
        ));
      }

      return new Licenses(licenses);
    }
  }

  @ReadingConverter
  private enum ToDocumentationsReadingConverter implements
      Converter<List<Document>, Documentations> {

    INSTANCE;

    @Override
    public Documentations convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<Documentation> documentations = new ArrayList<>(source.size());

      for (Document element : source) {
        documentations.add(new Documentation(
            element.getString("name"),
            element.getString("location"),
            element.getString("description"),
            ToTagsReadingConverter.INSTANCE.convert(element.getList("tags", String.class))
        ));
      }

      return new Documentations(documentations);
    }
  }

  @ReadingConverter
  private enum ToIntegrationsReadingConverter implements
      Converter<List<Document>, Integrations> {

    INSTANCE;

    @Override
    public Integrations convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<Integration> integrations = new ArrayList<>(source.size());

      for (Document element : source) {
        integrations.add(new Integration(
            element.getString("name"),
            element.getString("url"),
            element.getString("description"),
            ToTagsReadingConverter.INSTANCE.convert(element.getList("tags", String.class))
        ));
      }

      return new Integrations(integrations);
    }
  }


  @ReadingConverter
  private enum ToDeploymentsReadingConverter implements Converter<List<Document>, Deployments> {

    INSTANCE;

    @Override
    public Deployments convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<Deployment> deployments = new ArrayList<>(source.size());

      for (Document element : source) {
        deployments.add(new Deployment(
            element.getString("environment"),
            element.getString("location"),
            element.getString("version"),
            DateToLocalDateTimeConverter.INSTANCE.convert(element.getDate("timestamp"))
        ));
      }

      return new Deployments(deployments);
    }
  }

  @ReadingConverter
  private enum ToTagsReadingConverter implements Converter<List<String>, Tags> {

    INSTANCE;

    @Override
    public Tags convert(List<String> source) {
      return new Tags(source);
    }
  }

  @ReadingConverter
  private enum ToRolesReadingConverter implements Converter<List<String>, Roles> {

    INSTANCE;

    @Override
    public Roles convert(List<String> source) {
      return new Roles(source);
    }
  }

  @ReadingConverter
  private enum ToBranchesReadingConverter implements Converter<List<Document>, Branches> {

    INSTANCE;

    @Override
    public Branches convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<Branch> branches = new ArrayList<>(source.size());

      for (Document element : source) {
        branches.add(new Branch(element.getString("name")));
      }

      return new Branches(branches);
    }
  }

  @ReadingConverter
  private enum ToRepositoryTagsReadingConverter implements
      Converter<List<Document>, RepositoryTags> {

    INSTANCE;

    @Override
    public RepositoryTags convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<RepositoryTag> tags = new ArrayList<>(source.size());

      for (Document element : source) {
        tags.add(new RepositoryTag(
            element.getString("name"),
            DateToLocalDateTimeConverter.INSTANCE.convert(element.getDate("timestamp"))
        ));
      }

      return new RepositoryTags(tags);
    }
  }

  @ReadingConverter
  private enum ToCommitsReadingConverter implements Converter<List<Document>, Commits> {

    INSTANCE;

    @Override
    public Commits convert(List<Document> source) {
      if (source == null) {
        return null;
      }

      List<Commit> commits = new ArrayList<>(source.size());

      for (Document element : source) {
        commits.add(new Commit(
            element.getString("id"),
            DateToLocalDateTimeConverter.INSTANCE.convert(element.getDate("timestamp")),
            element.getString("shortMessage"),
            new CommitAuthor(
                element.get("author", Document.class).getString("name"),
                element.get("author", Document.class).getString("email")),
            new CommitChanges(
                new CommitLinesChanges(
                    commitChanges(element, "lines", "added"),
                    commitChanges(element, "lines", "deleted")),
                new CommitFilesChanges(
                    commitChanges(element, "files", "added"),
                    commitChanges(element, "files", "deleted"),
                    commitChanges(element, "files", "changed")
                )
            )));
      }

      return new Commits(commits);
    }

    private int commitChanges(Document document, String group, String key) {
      return document.get("changes", Document.class).get(group, Document.class).getInteger(key);
    }
  }
}