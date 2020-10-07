package tw.tonyyang.englishwords.data

import jxl.Workbook

interface ExcelDataSource {
    fun getData(url: String): Workbook
}