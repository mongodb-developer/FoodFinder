package mongodb.mobile.foodfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.mongodb.User
import mongodb.mobile.foodfinder.databinding.ActivityLocatorBinding

class LocatorActivity : AppCompatActivity() {

    private lateinit var locatorBinding: ActivityLocatorBinding
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locatorBinding = ActivityLocatorBinding.inflate(layoutInflater)
        setContentView(locatorBinding.root)

        user = foodFinderApp.currentUser()

        locatorBinding.btnSearch.setOnClickListener {
            validateAndSearch()
        }
        title = "Food Finder in New York"
    }

    private fun validateAndSearch() {
        if(!credentialsValid()){
            fieldsEmptyError("Both fields cannot be empty")
            return
        } else {
            val location = locatorBinding.etlocation.text.toString()
            val food = locatorBinding.etFood.text.toString()

            //once validated, pass the values to List Activity to display

            val intent = Intent(applicationContext, FoodLocationList::class.java)
            intent.putExtra("LOCATION", location)
            intent.putExtra("FOOD", food)
            startActivity(intent)

        }
    }

    private fun fieldsEmptyError(errorMsg: String) {

        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun credentialsValid(): Boolean = when {
        locatorBinding.etFood.text.toString().isEmpty() && locatorBinding.etlocation.text.isEmpty() -> false
        else-> true
    }
}