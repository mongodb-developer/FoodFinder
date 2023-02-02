package mongodb.mobile.foodfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmResults
import io.realm.mongodb.User
import io.realm.mongodb.sync.Subscription
import io.realm.mongodb.sync.SyncConfiguration
import mongodb.mobile.foodfinder.databinding.FoodLocationListBinding
import mongodb.mobile.foodfinder.models.Restaurant
import mongodb.mobile.foodfinder.recyclerView.FoodAdapter
import timber.log.Timber

class FoodLocationList : AppCompatActivity() {

    private lateinit var foodListBinding: FoodLocationListBinding
    private var user: User? = null
    private var food: String? = null
    private var location: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var config: SyncConfiguration
    private lateinit var realm: Realm
    private lateinit var locationList: RealmResults<Restaurant>

    private lateinit var adapter: FoodAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        foodListBinding = FoodLocationListBinding.inflate(layoutInflater)
        setContentView(foodListBinding.root)

        location = intent.getStringExtra("LOCATION")
        food = intent.getStringExtra("FOOD")

        title = location
        recyclerView = foodListBinding.rvList
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        checkUser()
    }

    private fun checkUser() {
        user = foodFinderApp.currentUser()
        Timber.d("User is $user ")

        if (user == null) {
            // if no user is currently logged in, start the login activity so the user can authenticate
            Timber.d("User is null")
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            initializeRealm()
        }
    }

    private fun initializeRealm() {
        config = SyncConfiguration.Builder(user!!).initialSubscriptions { realm, subscriptions ->
            subscriptions.addOrUpdate(
                Subscription.create(
                    "location",
                    realm.where(Restaurant::class.java)
                )
            )

        }.build()

        Realm.setDefaultConfiguration(config)

        //Instantiate a realm instance with the flexible sync configuration
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
               this@FoodLocationList.realm = realm
                onAfterRealmInit()
            }
        })
    }

    private fun onAfterRealmInit() {

        locationList = realm.where(Restaurant::class.java)
            .equalTo("borough", location)
            .and()
            .equalTo("cuisine", food)
            .findAllAsync()

        locationList.addChangeListener{ restaurantList: RealmResults<Restaurant>, _: OrderedCollectionChangeSet ->
            updateUI(restaurantList)
        }

    }

    override fun onStop() {
        super.onStop()
        if (this::realm.isInitialized) //realm is lateinit and realm may not be initialized
            realm.close()
    }

    private fun updateUI(restaurantList: RealmResults<Restaurant>) {
            Timber.d("Synced Restaurants count ${restaurantList.size}")
        foodListBinding.progress.visibility = View.GONE

        adapter = FoodAdapter(restaurantList)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}

