package tw.tonyyang.englishwords.di

import android.app.Application
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tw.tonyyang.englishwords.data.category.local.CategoryLocalDataSource
import tw.tonyyang.englishwords.data.category.local.CategoryLocalDataSourceImpl
import tw.tonyyang.englishwords.data.exam.local.ExamLocalDataSource
import tw.tonyyang.englishwords.data.exam.local.ExamLocalDataSourceImpl
import tw.tonyyang.englishwords.data.excel.local.ExcelLocalDataSource
import tw.tonyyang.englishwords.data.excel.remote.ExcelRemoteDataSource
import tw.tonyyang.englishwords.data.wordlist.local.WordListLocalDataSource
import tw.tonyyang.englishwords.data.wordlist.local.WordListLocalDataSourceImpl
import tw.tonyyang.englishwords.database.AppDatabase
import tw.tonyyang.englishwords.database.dao.WordDao
import tw.tonyyang.englishwords.repository.*
import tw.tonyyang.englishwords.ui.category.CategoryViewModel
import tw.tonyyang.englishwords.ui.exam.ExamViewModel
import tw.tonyyang.englishwords.ui.importer.ImporterViewModel
import tw.tonyyang.englishwords.ui.word.list.WordListViewModel

val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return AppDatabase.getDatabase(application)
    }

    fun provideWordDao(database: AppDatabase): WordDao {
        return database.wordDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideWordDao(get()) }
}

val appModule = module {
    // Importer
    viewModel { ImporterViewModel(get()) }
    single { ExcelRepository(get(), get()) }
    single { ExcelLocalDataSource() }
    single { ExcelRemoteDataSource() }
    // Exam
    viewModel { ExamViewModel(get()) }
    single<ExamRepository> { ExamRepositoryImpl(get()) }
    single<ExamLocalDataSource> { ExamLocalDataSourceImpl(get()) }
    // Category
    viewModel { CategoryViewModel(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<CategoryLocalDataSource> { CategoryLocalDataSourceImpl(get()) }
    // WordList
    viewModel { WordListViewModel(get()) }
    single<WordListRepository> { WordListRepositoryImpl(get()) }
    single<WordListLocalDataSource> { WordListLocalDataSourceImpl() }
}