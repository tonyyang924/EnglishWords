package tw.tonyyang.englishwords.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tw.tonyyang.englishwords.data.excel.local.ExcelLocalDataSource
import tw.tonyyang.englishwords.data.excel.remote.ExcelRemoteDataSource
import tw.tonyyang.englishwords.repository.ExcelRepository
import tw.tonyyang.englishwords.ui.importer.ImporterViewModel

val appModule = module {
    viewModel { ImporterViewModel(get()) }
    single { ExcelRepository(get(), get()) }
    single { ExcelLocalDataSource() }
    single { ExcelRemoteDataSource() }
}