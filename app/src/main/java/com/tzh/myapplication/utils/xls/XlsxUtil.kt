package com.tzh.myapplication.utils.xls

import android.annotation.SuppressLint
import android.content.Context
import com.tzh.myapplication.utils.ToastUtil
import com.tzh.mylibrary.util.voice.PathUtil
import jxl.Workbook
import jxl.format.Alignment
import jxl.format.VerticalAlignment
import jxl.write.Label
import jxl.write.WritableCellFormat
import jxl.write.WritableFont
import jxl.write.WriteException
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


object XlsxUtil {
    fun toXlsx(context: Context,list : MutableList<MyBean>){
        try {
            val titleList = arrayListOf("序号", "大象体重", "时间", "日期")

            val fileName = "记账记录_" + getNowTime() + ".xls"

            val resultPath = PathUtil.getXml(context).absolutePath + "/" + fileName

            val workbook = Workbook.createWorkbook(File(resultPath))
            // 设置sheet名称
            val sheet = workbook.createSheet("记账王-记录", 0)
            // 合并单元格
            sheet.mergeCells(0, 0, titleList.size - 1, 0)
            sheet.mergeCells(0, 1, titleList.size - 1, 1)

            // 调整列宽
            sheet.setRowView(0, 800)
            sheet.setRowView(1, 600)
            sheet.setRowView(2, 500)

            //创建字体，参数1：字体样式，参数2：字号，参数3：粗体
            val font = WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD);
            val wcTop = WritableCellFormat(font)
            wcTop.run {
                // 垂直居中
                alignment = Alignment.CENTRE
                verticalAlignment = VerticalAlignment.CENTRE
                wrap = true
            }
            // 顶部标题
            sheet.addCell(Label(0, 0, "记账记录导出表", wcTop))

            // 次级标题
            val wcTop2 = WritableCellFormat(
                WritableFont(
                    WritableFont.createFont("宋体"),
                    12
                )
            )
            wcTop2.run {
                alignment = Alignment.CENTRE
                verticalAlignment = VerticalAlignment.CENTRE
                wrap = true
            }
            //X月X日-Y月Y日
            sheet.addCell(Label(0, 1, "(0801 - 0815)", wcTop2))


            // 内容标题栏
            val startRow = 3

            titleList.forEachIndexed { index, title ->
                run {

                    val wc = WritableCellFormat(WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD))
                    wc.run {
                        verticalAlignment = VerticalAlignment.CENTRE
                        wrap = true
                    }
                    sheet.addCell(Label(index, startRow - 1, title, wc))
                }
            }

            // 内容
            list.forEachIndexed { index, excelBean ->
                run {
                    val wc = WritableCellFormat()
                    wc.wrap = true

                    val valueList = arrayListOf(
                        excelBean.id.toString(),
                        excelBean.weighValue,
                        excelBean.time,
                        excelBean.date
                    )
                    valueList.forEachIndexed { indexC, value ->
                        sheet.addCell(Label(indexC, index + startRow, value, wc))
                    }

                }
            }
            workbook.write()
            workbook.close()
            ShareImgUtil.shareImg(context,File(resultPath))
            ToastUtil.show("导出成功$resultPath")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: WriteException) {
            e.printStackTrace()
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun getNowTime(): String? {
        val time = Date()
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        return sdf.format(time)
    }
}