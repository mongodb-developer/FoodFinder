package mongodb.mobile.foodfinder

import android.app.Application
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import timber.log.Timber

lateinit var foodFinderApp: App

//replace this with your appId
const val APP_ID = "foodfinder-chwtl"

class FoodFinderApp: Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        Timber.plant(Timber.DebugTree())

        foodFinderApp = App(AppConfiguration.Builder(APP_ID).build())

    }
}