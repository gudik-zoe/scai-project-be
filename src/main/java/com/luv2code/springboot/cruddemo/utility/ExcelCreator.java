package com.luv2code.springboot.cruddemo.utility;


import com.luv2code.springboot.cruddemo.dto.Base64DTO;
import com.luv2code.springboot.cruddemo.entity.Post;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelCreator {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Post> listPosts;

    public ExcelCreator(List<Post> listPosts) {
        this.listPosts = listPosts;
        workbook = new XSSFWorkbook();
    }

    public void writeHeaderLine() {
        sheet = workbook.createSheet("accounts");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "id_post", style);
        createCell(row, 1, "date", style);
        createCell(row, 2, "text", style);
        createCell(row, 3, "image", style);
        createCell(row, 4, "status", style);

    }

    public void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Post post : listPosts) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, post.getIdPost(), style);
            createCell(row, columnCount++, post.getDate().toString(), style);
            createCell(row, columnCount++, post.getText(), style);
            createCell(row, columnCount++,post.getImage() , style);
            createCell(row, columnCount++, post.getStatus(), style);

        }
    }


    public void export() throws IOException {
        writeHeaderLine();
        writeDataLines();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        bos.close();
        workbook.close();
    }




    public Base64DTO exportToBase64(String fileName) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        bos.close();
        workbook.close();
        byte[] bytes = bos.toByteArray();
        Base64DTO excelDTO = new Base64DTO(fileName , Base64.encodeBase64String(bytes) , ".xls" );
        return excelDTO ;

    }


}

