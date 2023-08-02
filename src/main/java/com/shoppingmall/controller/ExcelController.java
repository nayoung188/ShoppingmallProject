package com.shoppingmall.controller;

import com.shoppingmall.entity.ProductEntity;
import com.shoppingmall.service.ProductService;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ExcelController {

    @Autowired
    private ProductService productService;

    @PostMapping("/downloadExcel")
    @ResponseBody
    public String productExcelDownload(@RequestParam(value = "productNo")Long[] productNo, HttpServletResponse response) throws IOException {

        List<ProductEntity> product = productService.getProductByNoLong(productNo);

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet("상품 리스트");

        CellStyle headStyle = workbook.createCellStyle();
        headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setAlignment(HorizontalAlignment.CENTER);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("상품번호");
        headerRow.createCell(1).setCellValue("판매상태");
        headerRow.createCell(2).setCellValue("상품명");
        headerRow.createCell(3).setCellValue("상품코드");
        headerRow.createCell(4).setCellValue("브랜드명");
        headerRow.createCell(5).setCellValue("제조사");
        headerRow.createCell(6).setCellValue("재고수");
        headerRow.createCell(7).setCellValue("정상가");
        headerRow.createCell(8).setCellValue("판매가");
        headerRow.createCell(9).setCellValue("상품등록일");
        headerRow.createCell(10).setCellValue("상품수정일");

        for(int i=0; i<11; i++){
            headerRow.getCell(i).setCellStyle(headStyle);
            sheet.trackAllColumnsForAutoSizing();
        }

        int rowNum = 1;
        for (ProductEntity prod : product){
            Row dataRow = sheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(prod.getProductNo());
            dataRow.createCell(1).setCellValue(prod.getProductState());
            dataRow.createCell(2).setCellValue(prod.getProductName());
            dataRow.createCell(3).setCellValue(prod.getProductCode());
            dataRow.createCell(4).setCellValue(prod.getProductBrand());
            dataRow.createCell(5).setCellValue(prod.getManufacturer());
            dataRow.createCell(6).setCellValue(prod.getProductStock());
            dataRow.createCell(7).setCellValue(prod.getOriginalPrice());
            dataRow.createCell(8).setCellValue(prod.getSalePrice());
            dataRow.createCell(9).setCellValue(prod.getCreateDate());
            dataRow.createCell(10).setCellValue(prod.getModifiedDate());
        }

        for(int i=0; i<11; i++){
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + (short)1500);
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String date = now.format(format);
        String fileName = "product_list_" + date + ".xlsx";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        workbook.write(response.getOutputStream());
        workbook.close();
        return "redirect:/dashboard/product";
    }
}
