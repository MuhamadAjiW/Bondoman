package com.example.bondoman.types.util

import android.content.Context
import com.example.bondoman.R
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.types.enums.ExcelTypes
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExcelUtil(val context: Context) {
    fun exportTransaction(
        transactionList: List<TransactionEntity>,
        type: ExcelTypes,
        path: File
    ): File {
        val ext = when (type){
            ExcelTypes.XLS -> ".xls"
            ExcelTypes.XLSX -> ".xlsx"
        }
        val file = File(
            path,
            "BondomanTransaction" + SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(
                Date()
            ) + ext
        )

        val workbook = XSSFWorkbook()
        val workSheet = workbook.createSheet("Transactions")

        val headerCellStyle = workbook.createCellStyle()
        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index)
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
        headerCellStyle.setBorderTop(BorderStyle.THIN)
        headerCellStyle.setBorderBottom(BorderStyle.THIN)
        headerCellStyle.setBorderLeft(BorderStyle.THIN)
        headerCellStyle.setBorderRight(BorderStyle.THIN)

        val headers = arrayOf(
            context.getString(R.string.transaction_label_number),
            context.getString(R.string.transaction_label_title),
            context.getString(R.string.transaction_label_category),
            context.getString(R.string.transaction_label_amount),
            context.getString(R.string.latitude),
            context.getString(R.string.longitude),
            context.getString(R.string.transaction_label_timestamp)
        )

        val cellStyle = headerCellStyle.copy()
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.index)
        cellStyle.wrapText = true

        val firstRow = workSheet.createRow(0)
        for ((index, header) in headers.withIndex()) {
            val cell = firstRow.createCell(index)
            cell.setCellValue(header)
            cell.cellStyle = headerCellStyle

            workSheet.setColumnWidth(index, 6000)
        }
        workSheet.setColumnWidth(0, 2000)

        for ((index, transaction) in transactionList.withIndex()){
            val row = workSheet.createRow(1 + index)

            var cell = row.createCell(0)
            cell.setCellValue((1 + index).toString())
            cell.cellStyle = cellStyle

            cell = row.createCell(1)
            cell.setCellValue(transaction.title)
            cell.cellStyle = cellStyle

            cell = row.createCell(2)
            cell.setCellValue(transaction.category)
            cell.cellStyle = cellStyle

            cell = row.createCell(3)
            cell.setCellValue(transaction.amount.toDouble())
            cell.cellStyle = cellStyle

            cell = row.createCell(4)
            transaction.latitude?.let { cell.setCellValue(it) }
            cell.cellStyle = cellStyle

            cell = row.createCell(5)
            transaction.longitude?.let { cell.setCellValue(it) }
            cell.cellStyle = cellStyle

            cell = row.createCell(6)
            cell.setCellValue(transaction.timestamp)
            cell.cellStyle = cellStyle
        }

        workbook.write(file.outputStream())
        workbook.close()

        return file
    }
}