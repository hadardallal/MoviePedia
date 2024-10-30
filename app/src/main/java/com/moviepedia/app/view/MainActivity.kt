package com.moviepedia.app.view

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.moviepedia.app.R
import com.moviepedia.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private var navController: NavController?=null
    private var menu:Menu?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow)

        navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        navController?.let { setupActionBarWithNavController(it) }
        binding.toolbar.setTitleTextColor(Color.WHITE)
        binding.toolbar.overflowIcon = ContextCompat.getDrawable(this,R.drawable.ic_more)

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.itemsFragment){
                binding.toolbar.navigationIcon = navIcon
                menu?.findItem(R.id.favourite_menu)?.isVisible = false
                menu?.findItem(R.id.location_menu)?.isVisible = false
                menu?.findItem(R.id.media_menu)?.isVisible = false
            }
            else{
                menu?.findItem(R.id.favourite_menu)?.isVisible = true
                menu?.findItem(R.id.location_menu)?.isVisible = true
                menu?.findItem(R.id.media_menu)?.isVisible = true
            }
            when (destination.id) {
                R.id.itemsFragment -> binding.toolbar.title = getString(R.string.app_name)
                R.id.addItemFragment -> binding.toolbar.title = getString(R.string.title_add_item_fragment)
                R.id.editItemFragment -> binding.toolbar.title = getString(R.string.title_edit_item_fragment)
                R.id.itemDetailFragment -> binding.toolbar.title = getString(R.string.title_item_detail_fragment)
                R.id.favouriteItemFragment -> binding.toolbar.title = getString(R.string.title_favourite_item_fragment)
                R.id.itemLocationFragment -> binding.toolbar.title = getString(R.string.title_location_item_fragment)
                R.id.itemMediaFragment -> binding.toolbar.title = getString(R.string.title_media_item_fragment)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favourite_menu){
            navController?.navigate(R.id.action_itemsFragment_to_favouriteItemFragment)
            return true
        }
        else if (item.itemId == R.id.location_menu){
            navController?.navigate(R.id.action_itemsFragment_to_locationItemFragment)
            return true
        }
        else if (item.itemId == R.id.media_menu){
            navController?.navigate(R.id.action_itemsFragment_to_mediaItemFragment)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        return navController?.navigateUp() ?: false
    }
}