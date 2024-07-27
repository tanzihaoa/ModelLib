package com.tzh.myapplication.utils.xls;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tzh.mylibrary.util.voice.PathUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DownloadDataUtil {
    public void download(Context context, List<MyBean> list){

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();

        //设置列宽
        sheet.setColumnWidth(0,2000);
        sheet.setColumnWidth(1,7000);
        sheet.setColumnWidth(2,7000);
        sheet.setColumnWidth(3,7000);

        //=================================定义表头属性===============================================
        HSSFFont font = wb.createFont(); // 生成字体格式设置对象
        font.setFontName("黑体"); // 设置字体黑体
        font.setBold(true); // 字体加粗
        font.setFontHeightInPoints(( short ) 16 ); // 设置字体大小
        font.setColor(HSSFFont.COLOR_NORMAL);//字体颜色

        HSSFCellStyle cellStyle = wb.createCellStyle(); // 生成行格式设置对象
        cellStyle.setBorderBottom(BorderStyle.THIN);// 下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
        cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 横向居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 纵向居中对齐
        cellStyle.setFont(font);

        //=================================定义内容属性===============================================
        HSSFFont txtContent = wb.createFont(); // 生成字体格式设置对象
        txtContent.setFontName("黑体"); // 设置字体黑体
        txtContent.setBold(false); // 字体加粗
        txtContent.setFontHeightInPoints(( short ) 12 ); // 设置字体大小
        txtContent.setColor(HSSFFont.COLOR_RED);//字体颜色

        HSSFCellStyle cellStyleContent = wb.createCellStyle(); // 生成行格式设置对象
        cellStyleContent.setBorderBottom(BorderStyle.THIN);// 下边框
        cellStyleContent.setBorderLeft(BorderStyle.THIN);// 左边框
        cellStyleContent.setBorderRight(BorderStyle.THIN);// 右边框
        cellStyleContent.setBorderTop(BorderStyle.THIN);// 上边框
        cellStyleContent.setAlignment(HorizontalAlignment.CENTER); // 横向居中对齐
        cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER); // 纵向居中对齐
        cellStyleContent.setFont(txtContent);

        //====================================写入数据===============================================
        for (int k=0;k<list.size()+1;k++){
            HSSFRow row = sheet.createRow(k);

            if (k==0){
                HSSFCell cell0=row.createCell(0);
                HSSFCell cell1=row.createCell(1);
                HSSFCell cell2=row.createCell(2);
                HSSFCell cell3=row.createCell(3);
                cell0.setCellStyle(cellStyle);
                cell1.setCellStyle(cellStyle);
                cell2.setCellStyle(cellStyle);
                cell3.setCellStyle(cellStyle);

                row.setHeight((short) 500);
                cell0.setCellValue("序号");
                cell1.setCellValue("大象体重");
                cell2.setCellValue("时间");
                cell3.setCellValue("日期");
            }else {

                HSSFCell cell0=row.createCell(0);
                HSSFCell cell1=row.createCell(1);
                HSSFCell cell2=row.createCell(2);
                HSSFCell cell3=row.createCell(3);
                cell0.setCellStyle(cellStyleContent);
                cell1.setCellStyle(cellStyleContent);
                cell2.setCellStyle(cellStyleContent);
                cell3.setCellStyle(cellStyleContent);

                row.setHeight((short) 500);
                cell0.setCellValue(list.get(k-1).getId());
                cell1.setCellValue(list.get(k-1).getWeighValue());
                cell2.setCellValue(list.get(k-1).getTime());
                cell3.setCellValue(list.get(k-1).getDate());
            }
        }

        String fileName = "我的文件名"+getNowTime()+".xls";

        String resultPath= PathUtil.getXml(context).getAbsolutePath() + "/" + fileName;

        File file = new File(resultPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e("fxHou","Create Fail IOException"+e);
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fileOutputStream=new FileOutputStream(resultPath);
            wb.write(fileOutputStream);
            fileOutputStream.close();
            Toast.makeText(context, "DOWNLOAD SUCCESS ："+resultPath, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.e("fxHou","Download Fail FileNotFoundException"+e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("fxHou","Download Fail IOException"+e);
            e.printStackTrace();
        }

    }

    private String getNowTime(){
        Date time=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(time);
    }
}
