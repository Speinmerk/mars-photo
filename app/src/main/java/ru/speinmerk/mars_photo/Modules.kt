package ru.speinmerk.mars_photo

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.koin.experimental.builder.factory
import org.koin.experimental.builder.single
import org.koin.experimental.builder.singleBy
import ru.speinmerk.mars_photo.data.source.PhotoRepository
import ru.speinmerk.mars_photo.data.source.PhotoRepositoryImpl
import ru.speinmerk.mars_photo.data.source.db.AppDatabase
import ru.speinmerk.mars_photo.data.source.network.PhotoApiService
import ru.speinmerk.mars_photo.data.source.prefs.SharedPreference
import ru.speinmerk.mars_photo.ui.main.MainPresenter

val appModule = module {
    factory<MainPresenter>()
}

val dataSourceModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "mars_photo_db")
            .build()
    }
//    fun providePhotoApiService(): PhotoApiService {
//        return Retrofit.Builder()
//            .baseUrl(BuildConfig.DOMAIN)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(PhotoApiService::class.java)
//    }

    single<AppExecutors>()
    single { provideDatabase(androidApplication()) }
    single<PhotoApiService>()
    single<SharedPreference>()
    singleBy<PhotoRepository, PhotoRepositoryImpl>()
}