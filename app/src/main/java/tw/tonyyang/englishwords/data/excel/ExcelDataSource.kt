package tw.tonyyang.englishwords.data.excel

import jxl.Workbook

interface ExcelDataSource {
    fun getData(url: String): Workbook
}