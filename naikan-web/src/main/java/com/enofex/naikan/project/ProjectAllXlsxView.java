package com.enofex.naikan.project;

import com.enofex.naikan.model.Bom;
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

final class ProjectAllXlsxView extends AbstractXlsxStreamingView {

  private static final Map<String, Function<Bom, String>> COLUMNS = new LinkedHashMap<>() {{
    put("Id", Bom::id);
    put("Timestamp", bom -> bom.timestamp() != null ? bom.timestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) :"");
    put("Name", bom -> bom.project().name());
    put("URL", bom -> bom.project().url());
    put("Repository", bom -> bom.project().repository());
    put("Packaging", bom -> bom.project().packaging());
    put("Group", bom -> bom.project().groupId());
    put("Artifact", bom -> bom.project().artifactId());
    put("Version", bom -> bom.project().version());
    put("Description", bom -> bom.project().description());
    put("Notes", bom -> bom.project().notes());
    put("Organization Name", bom -> bom.organization() != null ? bom.organization().name(): "");
    put("Organization URL", bom -> bom.organization() != null ? bom.organization().url() : "");
    put("Organization Department", bom -> bom.organization() != null ? bom.organization().department(): "");
    put("Organization Description", bom -> bom.organization() != null ? bom.organization().description(): "");
    put("Environments", bom -> String.valueOf(bom.environments().all().size()));
    put("Teams", bom -> String.valueOf(bom.teams().all().size()));
    put("Developers", bom -> String.valueOf(bom.developers().all().size()));
    put("Contacts", bom -> String.valueOf(bom.contacts().all().size()));
    put("Documentations", bom -> String.valueOf(bom.documentations().all().size()));
    put("Integrations", bom -> String.valueOf(bom.integrations().all().size()));
    put("Technologies", bom -> String.valueOf(bom.technologies().all().size()));
    put("Deployments", bom -> String.valueOf(bom.deployments().all().size()));
    put("Licenses", bom -> String.valueOf(bom.licenses().all().size()));
  }};

  private final List<Bom> boms;

  ProjectAllXlsxView(List<Bom> boms) {
    this.boms = boms;
  }

  @Override
  protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
      HttpServletRequest request, HttpServletResponse response) {
    Sheet sheet = workbook.createSheet("Projects");

    writeHeader(sheet);
    writeRows(sheet);
  }

  private void writeHeader(Sheet sheet) {
    Row header = sheet.createRow(0);

    COLUMNS.keySet().forEach(name ->
        header.createCell(lastCellNum(header.getLastCellNum())).setCellValue(name)
    );
  }

  private void writeRows(Sheet sheet) {
    if (CollectionUtils.isNotEmpty(this.boms)) {
      for (int r = 0, size = this.boms.size(); r < size; r++) {
        Bom bom = this.boms.get(r);
        Row row = sheet.createRow(r + 1);

        for (Entry<String, Function<Bom, String>> entry : COLUMNS.entrySet()) {
          row.createCell(lastCellNum(row.getLastCellNum()))
              .setCellValue(entry.getValue().apply(bom));
        }
      }
    }
  }

  private short lastCellNum(short lastCellNum) {
    return lastCellNum == -1 ? 0 : lastCellNum;
  }
}
