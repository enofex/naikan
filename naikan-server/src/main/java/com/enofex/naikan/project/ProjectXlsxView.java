package com.enofex.naikan.project;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Branch;
import com.enofex.naikan.model.Commit;
import com.enofex.naikan.model.Contact;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.Developer;
import com.enofex.naikan.model.Documentation;
import com.enofex.naikan.model.Environment;
import com.enofex.naikan.model.Integration;
import com.enofex.naikan.model.License;
import com.enofex.naikan.model.RepositoryTag;
import com.enofex.naikan.model.Team;
import com.enofex.naikan.model.Technology;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

final class ProjectXlsxView extends AbstractXlsxStreamingView {

  private final Bom bom;

  ProjectXlsxView(Bom bom) {
    this.bom = bom;
  }

  @Override
  protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
      HttpServletRequest request, HttpServletResponse response) {
    overview(workbook);
    environments(workbook);
    teams(workbook);
    developers(workbook);
    contacts(workbook);
    documentations(workbook);
    integrations(workbook);
    technologies(workbook);
    deployments(workbook);
    licenses(workbook);
    repository(workbook);
    commits(workbook);
  }

  private void overview(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Overview");
    sheet.createRow(0).createCell(0).setCellValue(this.bom.id());

    if (this.bom.organization() != null) {
      emptyRow(sheet);
      row(sheet, "Organization");
      row(sheet, "Name", this.bom.organization().name());
      row(sheet, "URL", this.bom.organization().url());
      row(sheet, "Department", this.bom.organization().department());
      row(sheet, "Description", this.bom.organization().description());
    }

    if (this.bom.project() != null) {
      emptyRow(sheet);
      row(sheet, "Project");
      row(sheet, "Name", this.bom.project().name());
      row(sheet, "Inception Year", this.bom.project().inceptionYear());
      row(sheet, "URL", this.bom.project().url());
      row(sheet, "Repository", this.bom.project().repository());
      row(sheet, "Packaging", this.bom.project().packaging());
      row(sheet, "Group", this.bom.project().groupId());
      row(sheet, "Artifact", this.bom.project().artifactId());
      row(sheet, "Description", this.bom.project().description());
      row(sheet, "Notes", this.bom.project().notes());
      row(sheet, "Tags", String.join(", ", this.bom.tags().all()));
    }
  }

  private void environments(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Environments");
    Map<String, Function<Environment, String>> columns = new LinkedHashMap<>() {{
      put("Name", Environment::name);
      put("Location", Environment::location);
      put("Description", Environment::description);
      put("Tags", env -> String.join(", ", env.tags().all()));
    }};

    writeRows(sheet, columns, this.bom.environments().all());
  }

  private void teams(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Teams");
    Map<String, Function<Team, String>> columns = new LinkedHashMap<>() {{
      put("Name", Team::name);
      put("Description", Team::description);
    }};

    writeRows(sheet, columns, this.bom.teams().all());
  }

  private void developers(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Developers");
    Map<String, Function<Developer, String>> columns = new LinkedHashMap<>() {{
      put("Name", Developer::name);
      put("Username", Developer::username);
      put("Title", Developer::title);
      put("Organization", Developer::organization);
      put("Organization URL", Developer::organizationUrl);
      put("Department", Developer::department);
      put("Email", Developer::email);
      put("Phone", Developer::phone);
      put("Timezone", Developer::timezone);
      put("Description", Developer::description);
      put("Roles", developer -> String.join(", ", developer.roles().all()));
    }};

    writeRows(sheet, columns, this.bom.developers().all());
  }

  private void contacts(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Contacts");
    Map<String, Function<Contact, String>> columns = new LinkedHashMap<>() {{
      put("Name", Contact::name);
      put("Title", Contact::title);
      put("Email", Contact::email);
      put("Phone", Contact::phone);
      put("Description", Contact::description);
      put("Roles", contact -> String.join(", ", contact.roles().all()));
    }};

    writeRows(sheet, columns, this.bom.contacts().all());
  }

  private void documentations(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Documentations");
    Map<String, Function<Documentation, String>> columns = new LinkedHashMap<>() {{
      put("Name", Documentation::name);
      put("Location", Documentation::location);
      put("Description", Documentation::description);
      put("Tags",
          documentation -> String.join(", ", documentation.tags().all()));
    }};

    writeRows(sheet, columns, this.bom.documentations().all());
  }

  private void integrations(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Integrations");
    Map<String, Function<Integration, String>> columns = new LinkedHashMap<>() {{
      put("Name", Integration::name);
      put("URL", Integration::url);
      put("Description", Integration::description);
      put("Tags",
          documentation -> String.join(", ", documentation.tags().all()));
    }};

    writeRows(sheet, columns, this.bom.integrations().all());
  }

  private void technologies(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Technologies");
    Map<String, Function<Technology, String>> columns = new LinkedHashMap<>() {{
      put("Name", Technology::name);
      put("Version", Technology::version);
      put("Description", Technology::description);
      put("Tags",
          technology -> String.join(", ", technology.tags().all()));
    }};

    writeRows(sheet, columns, this.bom.technologies().all());
  }

  private void deployments(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Deployments");
    Map<String, Function<Deployment, String>> columns = new LinkedHashMap<>() {{
      put("Environment", Deployment::environment);
      put("Location", Deployment::location);
      put("Timestamp", deployment -> deployment.timestamp() != null ? deployment.timestamp()
          .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "");
      put("Version", Deployment::version);
    }};

    writeRows(sheet, columns, this.bom.deployments().all());
  }

  private void licenses(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Licenses");
    Map<String, Function<License, String>> columns = new LinkedHashMap<>() {{
      put("Name", License::name);
      put("URL", License::url);
      put("Description", License::description);
    }};

    writeRows(sheet, columns, this.bom.licenses().all());
  }

  private void repository(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Repository");

    if (this.bom.repository() != null) {
      Row row = sheet.createRow(0);
      row.createCell(0).setCellValue("Name");
      row.createCell(1).setCellValue(this.bom.repository().name());
      row = sheet.createRow(sheet.getLastRowNum() + 1);
      row.createCell(0).setCellValue("URL");
      row.createCell(1).setCellValue(this.bom.repository().url());
      row = sheet.createRow(sheet.getLastRowNum() + 1);
      row.createCell(0).setCellValue("Default branch");
      row.createCell(1).setCellValue(this.bom.repository().defaultBranch());
      row = sheet.createRow(sheet.getLastRowNum() + 1);

      if (this.bom.repository().firstCommit() != null) {
        row.createCell(0).setCellValue("First commit");
        row.createCell(1).setCellValue("%s on %s by %s %s".formatted(
                this.bom.repository().firstCommit().commitId(),
                this.bom.repository().firstCommit().timestamp()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                this.bom.repository().firstCommit().author().name(),
                this.bom.repository().firstCommit().author().email()
            )
        );
      }

      if (this.bom.repository().branches() != null &&
          CollectionUtils.isNotEmpty(this.bom.repository().branches().all())) {
        branches(sheet);
      }

      if (this.bom.repository().tags() != null &&
          CollectionUtils.isNotEmpty(this.bom.repository().tags().all())) {
        tags(sheet);
      }
    }
  }

  private void branches(Sheet sheet) {
    sheet.createRow(sheet.getLastRowNum() + 1);
    Row header = sheet.createRow(sheet.getLastRowNum() + 1);
    header.createCell(0).setCellValue("Branches");
    Map<String, Function<Branch, String>> columns = new LinkedHashMap<>() {{
      put("Name", Branch::name);
    }};

    writeRows(sheet, columns,
        this.bom.repository() != null ? this.bom.repository().branches().all() : List.of());
  }

  private void tags(Sheet sheet) {
    sheet.createRow(sheet.getLastRowNum() + 1);
    Row header = sheet.createRow(sheet.getLastRowNum() + 1);
    header.createCell(0).setCellValue("Tags");
    Map<String, Function<RepositoryTag, String>> columns = new LinkedHashMap<>() {{
      put("Name", RepositoryTag::name);
      put("Timestamp", tag -> tag.timestamp() != null ? tag.timestamp()
          .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "");
    }};

    writeRows(sheet, columns,
        this.bom.repository() != null ? this.bom.repository().tags().all() : List.of());
  }

  private void commits(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Commits");

    if (this.bom.repository() != null) {
      Map<String, Function<Commit, String>> columns = new LinkedHashMap<>() {{
        put("Author name", commit -> commit.author().name());
        put("Author email", commit -> commit.author().email());
        put("Commit", Commit::commitId);
        put("Timestamp", commit -> commit.timestamp() != null ? commit.timestamp()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "");
        put("Message", Commit::shortMessage);
        put("Line added", commit -> String.valueOf(commit.changes().lines().added()));
        put("Line deleted", commit -> String.valueOf(commit.changes().lines().deleted()));
        put("File added", commit -> String.valueOf(commit.changes().files().added()));
        put("File deleted", commit -> String.valueOf(commit.changes().files().deleted()));
        put("File changed", commit -> String.valueOf(commit.changes().files().changed()));
      }};

      writeRows(sheet, columns, this.bom.repository().commits().all());
    }
  }

  private void emptyRow(Sheet sheet) {
    sheet.createRow(sheet.getLastRowNum() + 1);
  }

  private void row(Sheet sheet, String name) {
    Row row = sheet.createRow(sheet.getLastRowNum() + 1);
    row.createCell(0).setCellValue(name);
  }

  private void row(Sheet sheet, String name, String value) {
    Row row = sheet.createRow(sheet.getLastRowNum() + 1);
    row.createCell(0).setCellValue(name);
    row.createCell(1).setCellValue(value);
  }

  private <T> void writeRows(Sheet sheet, Map<String, Function<T, String>> columns,
      List<T> elements) {
    writeHeader(sheet, columns);

    if (CollectionUtils.isNotEmpty(elements)) {
      for (T element : elements) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        for (Entry<String, Function<T, String>> entry : columns.entrySet()) {
          row.createCell(lastCellNum(row.getLastCellNum()))
              .setCellValue(entry.getValue().apply(element));
        }
      }
    }
  }

  private <T> void writeHeader(Sheet sheet, Map<String, Function<T, String>> columns) {
    Row header = sheet.createRow(sheet.getLastRowNum() == -1 ? 0 : sheet.getLastRowNum() + 1);

    columns.keySet().forEach(name ->
        header.createCell(lastCellNum(header.getLastCellNum())).setCellValue(name)
    );
  }

  private short lastCellNum(short lastCellNum) {
    return lastCellNum == -1 ? 0 : lastCellNum;
  }
}
