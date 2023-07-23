package com.enofex.naikan.project;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Contact;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.Developer;
import com.enofex.naikan.model.Documentation;
import com.enofex.naikan.model.Environment;
import com.enofex.naikan.model.Integration;
import com.enofex.naikan.model.License;
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
import java.util.stream.Collectors;
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
      row(sheet, "URL", this.bom.project().url());
      row(sheet, "Repository", this.bom.project().repository());
      row(sheet, "Packaging", this.bom.project().packaging());
      row(sheet, "Group", this.bom.project().groupId());
      row(sheet, "Artifact", this.bom.project().artifactId());
      row(sheet, "Description", this.bom.project().description());
      row(sheet, "Notes", this.bom.project().notes());
      row(sheet, "Tags", this.bom.tags().all().stream().collect(Collectors.joining(", ")));
    }
  }

  private void environments(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Environments");
    Map<String, Function<Environment, String>> columns = new LinkedHashMap<>() {{
      put("Name", Environment::name);
      put("Location", Environment::location);
      put("Description", Environment::description);
      put("Tags", env -> env.tags().all().stream().collect(Collectors.joining(", ")));
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
      put("Roles", developer -> developer.roles().all().stream().collect(Collectors.joining(", ")));
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
      put("Roles", contact -> contact.roles().all().stream().collect(Collectors.joining(", ")));
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
          documentation -> documentation.tags().all().stream().collect(Collectors.joining(", ")));
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
          documentation -> documentation.tags().all().stream().collect(Collectors.joining(", ")));
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
          technology -> technology.tags().all().stream().collect(Collectors.joining(", ")));
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
      for (int r = 0, size = elements.size(); r < size; r++) {
        T element = (T) elements.get(r);
        Row row = sheet.createRow(r + 1);

        for (Entry<String, Function<T, String>> entry : columns.entrySet()) {
          row.createCell(lastCellNum(row.getLastCellNum()))
              .setCellValue(entry.getValue().apply(element));
        }
      }
    }
  }

  private <T> void writeHeader(Sheet sheet, Map<String, Function<T, String>> columns) {
    Row header = sheet.createRow(0);

    columns.keySet().forEach(name ->
        header.createCell(lastCellNum(header.getLastCellNum())).setCellValue(name)
    );
  }

  private short lastCellNum(short lastCellNum) {
    return lastCellNum == -1 ? 0 : lastCellNum;
  }
}
