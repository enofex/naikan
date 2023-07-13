package com.enofex.naikan;

import com.enofex.naikan.model.AbstractContainer;
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
import com.enofex.naikan.model.Roles;
import com.enofex.naikan.model.Tags;
import com.enofex.naikan.model.Team;
import com.enofex.naikan.model.Teams;
import com.enofex.naikan.model.Technologies;
import com.enofex.naikan.model.Technology;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
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
        ToDeploymentsReadingConverter.INSTANCE));
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
      Converter<List<Environment>, Environments> {

    INSTANCE;

    @Override
    public Environments convert(List<Environment> source) {
      return new Environments(source);
    }
  }

  @ReadingConverter
  private enum ToTeamsReadingConverter implements Converter<List<Team>, Teams> {

    INSTANCE;

    @Override
    public Teams convert(List<Team> source) {
      return new Teams(source);
    }
  }

  @ReadingConverter
  private enum ToDevelopersReadingConverter implements Converter<List<Developer>, Developers> {

    INSTANCE;

    @Override
    public Developers convert(List<Developer> source) {
      return new Developers(source);
    }
  }

  @ReadingConverter
  private enum ToContactsReadingConverter implements Converter<List<Contact>, Contacts> {

    INSTANCE;

    @Override
    public Contacts convert(List<Contact> source) {
      return new Contacts(source);
    }
  }

  @ReadingConverter
  private enum ToTechnologiesReadingConverter implements Converter<List<Technology>, Technologies> {

    INSTANCE;

    @Override
    public Technologies convert(List<Technology> source) {
      return new Technologies(source);
    }
  }

  @ReadingConverter
  private enum ToLicencesReadingConverter implements Converter<List<License>, Licenses> {

    INSTANCE;

    @Override
    public Licenses convert(List<License> source) {
      return new Licenses(source);
    }
  }

  @ReadingConverter
  private enum ToDocumentationsReadingConverter implements
      Converter<List<Documentation>, Documentations> {

    INSTANCE;

    @Override
    public Documentations convert(List<Documentation> source) {
      return new Documentations(source);
    }
  }

  @ReadingConverter
  private enum ToIntegrationsReadingConverter implements
      Converter<List<Integration>, Integrations> {

    INSTANCE;

    @Override
    public Integrations convert(List<Integration> source) {
      return new Integrations(source);
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
  private enum ToDeploymentsReadingConverter implements Converter<List<Deployment>, Deployments> {

    INSTANCE;

    @Override
    public Deployments convert(List<Deployment> source) {
      return new Deployments(source);
    }
  }
}