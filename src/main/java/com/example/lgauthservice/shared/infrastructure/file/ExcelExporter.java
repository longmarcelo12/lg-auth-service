package com.example.lgauthservice.shared.infrastructure.file;

import com.example.lgauthservice.shared.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@ConditionalOnClass(name = "org.apache.poi.ss.usermodel.Workbook")
public class ExcelExporter {

    @Value("${config.report-template.folder}")
    private String reportTemplateFolder;

    public ByteArrayInputStream exportList(List<?> dtos, String templateFilePath, Map<String, String> titles) {
        try {
            ClassPathResource res = new ClassPathResource(reportTemplateFolder + Constants.SLASH + templateFilePath);
            Workbook workbook = new XSSFWorkbook(res.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            replaceTitlePlaceholdersInSheet(sheet, titles);

            Integer templateRowIndex = findTemplateRow(sheet);
            if (ObjectUtils.allNotNull(templateRowIndex)) {
                Row templateRow = sheet.getRow(templateRowIndex);
                for (int i = 0; i < dtos.size(); i++) {
                    Object dto = dtos.get(i);
                    Row newRow = sheet.createRow(templateRowIndex + i + 1);
                    copyRow(templateRow, newRow);
                    fillRowWithDto(newRow, dto);
//                fillRowWithParams(newRow, dto);
                }
                removeTemplateRow(sheet, templateRowIndex);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            log.error("ExcelExporter exportList error: {}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    private Integer findTemplateRow(Sheet sheet) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING &&
                        cell.getStringCellValue().contains("{{data.")) {
                    return row.getRowNum();
                }
            }
        }
        return null;
    }

    private void copyRow(Row src, Row dest) {
        for (int i = 0; i < src.getLastCellNum(); i++) {
            Cell oldCell = src.getCell(i);
            if (oldCell == null) continue;

            Cell newCell = dest.createCell(i);
            newCell.setCellStyle(oldCell.getCellStyle());

            if (oldCell.getCellType() == CellType.STRING) {
                newCell.setCellValue(oldCell.getStringCellValue());
            }
        }
    }

    private void fillRowWithDto(Row row, Object dto) throws Exception {
        for (Cell cell : row) {
//            if (cell.getCellType() != CellType.STRING) continue;
            String value = cell.getStringCellValue();

            // tìm các placeholder dạng {{...}}
            Pattern pattern = Pattern.compile("\\{\\{(data\\.[^}]+)}}");
            Matcher matcher = pattern.matcher(value);

            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String placeholder = matcher.group(1).trim();
                String startWith = "data.";
                if (placeholder.startsWith(startWith)) {
                    String fieldPath = placeholder.substring(startWith.length());
                    Object fieldValue = getNestedField(dto, fieldPath);

                    matcher.appendReplacement(sb,
                            fieldValue == null ? "" : fieldValue.toString());
                }
            }
            matcher.appendTail(sb);
            cell.setCellValue(sb.toString());
        }
    }

    private Object getNestedField(Object obj, String fieldPath) throws Exception {
        String[] fields = fieldPath.split("\\.");
        Object current = obj;
        for (String f : fields) {
            Field field = current.getClass().getDeclaredField(f);
            field.setAccessible(true);
            current = field.get(current);

            if (current == null) return null;
        }
        return current;
    }

    private void removeTemplateRow(Sheet sheet, int templateRowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (templateRowIndex >= 0 && templateRowIndex < lastRowNum) {
            sheet.shiftRows(templateRowIndex + 1, lastRowNum, -1);
        }
        Row row = sheet.getRow(lastRowNum);
        if (row != null) sheet.removeRow(row);
    }

    private void fillRowWithParams(Row row, Map<String, Object> params) {
        for (Cell cell : row) {
            if (cell.getCellType() != CellType.STRING) continue;

            String value = cell.getStringCellValue();
            String newValue = replacePlaceholders(value, params);
            cell.setCellValue(newValue);
        }
    }

    private String replacePlaceholders(String text, Map<String, Object> params) {
        Pattern pattern = Pattern.compile("\\{\\{([^}]+)}}");
        Matcher matcher = pattern.matcher(text);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1).trim();
            Object value = resolveValueByPath(key, params);
            matcher.appendReplacement(sb, value != null ? value.toString() : "");
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private Object resolveValueByPath(String path, Map<String, Object> params) {
        String[] parts = path.split("\\.");

        Object current = params.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            if (current == null) return null;

            try {
                Field field = current.getClass().getDeclaredField(parts[i]);
                field.setAccessible(true);
                current = field.get(current);
            } catch (Exception ex) {
                return null;
            }
        }
        return current;
    }

    public void replaceTitlePlaceholdersInSheet(Sheet sheet, Map<String, String> titles) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.STRING) continue;
                String cellValue = cell.getStringCellValue();
                if (cellValue.contains("{{title.")) {
                    cellValue = replaceTitlePlaceholders(cellValue, titles);
                    cell.setCellValue(cellValue);
                }
            }
        }
    }

    private String replaceTitlePlaceholders(String text, Map<String, String> titles) {
        Pattern pattern = Pattern.compile("\\{\\{title\\.([a-zA-Z0-9_]+)}}");
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1); // fromDate, toDate, ...
            String value = titles.getOrDefault(key, "");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    public byte[] encryptExcelFile(byte[] excelBytes, String password) {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             POIFSFileSystem fs = new POIFSFileSystem()) {

            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            Encryptor encryptor = info.getEncryptor();
            encryptor.confirmPassword(password);

            try (OPCPackage opc = OPCPackage.open(new ByteArrayInputStream(excelBytes));
                 OutputStream os = encryptor.getDataStream(fs)) {
                opc.save(os);
            }

            fs.writeFilesystem(bos);
            return bos.toByteArray();

        } catch (Exception e) {
            log.error("Lỗi khi đặt mật khẩu cho file Excel: ", e);
            return excelBytes;
        }
    }
}
