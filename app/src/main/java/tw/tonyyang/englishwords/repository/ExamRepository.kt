package tw.tonyyang.englishwords.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tw.tonyyang.englishwords.data.exam.local.ExamLocalDataSource
import tw.tonyyang.englishwords.database.entity.Word

interface ExamRepository {
    suspend fun getRandomWords(limitNum: Int): Flow<List<Word>>
}

class ExamRepositoryImpl(private val examLocalDataSource: ExamLocalDataSource) : ExamRepository {
    override suspend fun getRandomWords(limitNum: Int): Flow<List<Word>> = flow {
        emit(examLocalDataSource.getRandomWords(limitNum))
    }
}