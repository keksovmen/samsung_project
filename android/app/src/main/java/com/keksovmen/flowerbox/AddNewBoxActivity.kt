package com.keksovmen.flowerbox

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.keksovmen.flowerbox.databinding.ActivityConnectBoxBinding

class AddNewBoxActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityConnectBoxBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectBoxBinding.inflate(
            layoutInflater
        )
        setContentView(binding.getRoot())
        setSupportActionBar(binding.toolbar)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_add_new_box) as NavHostFragment).navController
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment_content_add_new_box)
        return (navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp())
    }
}