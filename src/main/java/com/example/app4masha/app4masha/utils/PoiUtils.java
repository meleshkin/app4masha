package com.example.app4masha.app4masha.utils;

import com.example.app4masha.app4masha.data.OrganizationAndPunishment;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PoiUtils {


    public Set<OrganizationAndPunishment> getOrganizationAndPunishmentFromInfoFile(String filename) throws IOException {
        Set<OrganizationAndPunishment> result = new HashSet<>();
        String organization = null;
        String punishment = null;

        Workbook workbook;
        try (FileInputStream excelFile = new FileInputStream(new File(filename))) {
            workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> rows = datatypeSheet.iterator();
            boolean header = true;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (header) {
                    header = false;
                    continue;
                }
                Iterator<Cell> cellIterator = currentRow.iterator();

                int cellCount = 0;
                while (cellIterator.hasNext()) {

                    Cell currentCell = cellIterator.next();
                    cellCount++;
                    if (cellCount == 2) {
                        organization = currentCell.getStringCellValue();
                        //organization = organization.replaceAll("Общество с ограниченной ответственностью", "OOO");
                    } else if (cellCount == 7) {
                        punishment = currentCell.getStringCellValue();
                    }
                    if (organization != null && !organization.isEmpty() && punishment != null && !punishment.isEmpty()) {
                        result.add(new OrganizationAndPunishment(organization, punishment));
                        organization = null;
                        punishment = null;
                    }
                }
            }
        }
        return result;
    }

    public static String processDirectory(String workDir, String ribaFilename, String infoFilename) throws Exception {
        Collection<OrganizationAndPunishment> kontoraAndPunishment = new PoiUtils().getOrganizationAndPunishmentFromInfoFile(workDir + "/" + infoFilename);
        File[] outputFiles = new File[kontoraAndPunishment.size()+1];
        AtomicInteger fileCount = new AtomicInteger(0);
        StringBuilder dictBuilder = new StringBuilder();
        kontoraAndPunishment.forEach(map -> {
            String organization = map.getOrganizationName();
            String punishment = map.getPunishment();
            //String outputFile = workDir + "/" + organization.replaceAll("\"", "").replaceAll("«", "").replaceAll("»", "")+".docx";
            //String outputFile = Paths.get(workDir, latinedOrganization + ".docx").normalize().toString();
            String fName = fileCount.get() + ".docx";
            String outputFile = Paths.get(workDir, fName).normalize().toString();
            try {
                //XWPFDocument doc = new XWPFDocument(OPCPackage.open(workDir + "/" +  ribaFilename));
                XWPFDocument doc = new XWPFDocument(OPCPackage.open(Paths.get(workDir,ribaFilename).normalize().toString()));
                processTables(doc, organization, punishment);
                processParagraphes(doc, organization, punishment);
                FileOutputStream fis = new FileOutputStream(outputFile);

                dictBuilder.append(fileCount.get());
                dictBuilder.append(" ");
                dictBuilder.append(organization);
                dictBuilder.append("\n");
                outputFiles[fileCount.getAndIncrement()] = Paths.get(workDir, fName).normalize().toFile();


                doc.write(fis);
                //doc.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(workDir, "dict.txt").normalize().toFile()));
            writer.write(dictBuilder.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (fileCount.get() > 0) {
            File output =  new File(workDir + "/result.zip");
            outputFiles[outputFiles.length-1] = Paths.get(workDir, "dict.txt").normalize().toFile();
            ZipUtil.packEntries(outputFiles, output);
            return output.getAbsolutePath();
        }




        return null;
    }

    private static void processParagraphes(XWPFDocument doc, String kontora, String punishment) throws Exception {
        boolean atFound = false;
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null) {
                        if (atFound) {
                            String atText = r.getText(0);
                            if (atText.equalsIgnoreCase("Kontora")) {
                                r.setText(kontora, 0);
                            } else if (atText.equalsIgnoreCase("Punishment")) {
                                r.setText(" " + punishment, 0);
                            } else {
                                // other placeholders
                            }
                            atFound = false;
                        } else {
                            if (text.contains("@")) {
                                r.setText("", 0);
                                atFound = true;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void processTables(XWPFDocument doc, String kontora, String punishment) throws Exception {
        boolean atFound = false;
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        List<XWPFRun> runs = p.getRuns();
                        if (runs != null) {
                            for (XWPFRun r : runs) {
                                String text = r.getText(0);
                                if (text != null) {
                                    if (atFound) {
                                        String atText = r.getText(0);
                                        if (atText.equalsIgnoreCase("Date")) {
                                            r.setText(getCurrentDateStrLocalized(), 0);
                                            //System.out.println("Date -> " + getCurrentDateStrLocalized());
                                        } else if (atText.equalsIgnoreCase("TimeStart")) {
                                            r.setText("14ч 00мин.", 0);
                                        } else if (atText.equalsIgnoreCase("TimeFinish")) {
                                            r.setText("15ч 00мин.", 0);
                                        }
                                        atFound = false;
                                    } else {
                                        if (text.startsWith("@")) {
                                            r.setText("", 0);
                                            atFound = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static String getCurrentDateStrLocalized() {
        ZonedDateTime zoned = ZonedDateTime.now();
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(new Locale("ru", "RU"));
        return zoned.format(pattern);
    }
}
