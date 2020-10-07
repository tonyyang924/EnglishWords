package tw.tonyyang.englishwords.repository

import jxl.Sheet
import jxl.Workbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.data.local.ExcelLocalDataSource
import tw.tonyyang.englishwords.data.remote.ExcelRemoteDataSource
import tw.tonyyang.englishwords.database.Word
import java.lang.IllegalArgumentException


class ExcelRepository(
        private val localDataSource: ExcelLocalDataSource = ExcelLocalDataSource(),
        private val remoteDataSource: ExcelRemoteDataSource = ExcelRemoteDataSource()
) {
    suspend fun getWordList(fileUrl: String?): List<Word> = withContext(Dispatchers.IO) {
        if (fileUrl.isNullOrBlank()) {
            throw IllegalArgumentException(App.appContext.getString(R.string.import_excel_failed))
        }
        return@withContext mutableListOf<Word>().apply {
            val workbook = getWorkbook(fileUrl)
            workbook.sheets.forEach { sheet ->
                addAll(sheet.parseToWordList())
            }
            workbook.close()
        }
    }

    private fun getWorkbook(fileUrl: String): Workbook =
            if (fileUrl.contains(SCHEME_CONTENT) || fileUrl.contains(SCHEMA_FILE)) {
                localDataSource.getData(fileUrl)
            } else {
                remoteDataSource.getData(fileUrl)
            }

    private fun Sheet.parseToWordList(): List<Word> {
        val wordList = mutableListOf<Word>()
        for (i in 0 until rows) {
            if (getCell(0, i).contents[0].toString() == "#") continue
            wordList.add(Word(
                    word = getCell(0, i).contents,
                    wordMean = getCell(1, i).contents,
                    category = getCell(2, i).contents,
                    wordStar = getCell(3, i).contents,
                    wordSentence = getCell(4, i).contents
            ))
        }
        return wordList
    }

    companion object {
        private const val SCHEME_CONTENT = "content://"
        private const val SCHEMA_FILE = "file:///"
    }
}