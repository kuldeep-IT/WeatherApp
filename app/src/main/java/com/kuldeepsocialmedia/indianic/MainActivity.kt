package com.kuldeepsocialmedia.indianic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kuldeepsocialmedia.indianic.Authentication.Otp
import com.kuldeepsocialmedia.indianic.databinding.ActivityMainBinding
import com.kuldeepsocialmedia.indianic.map.MapsActivity
import com.kuldeepsocialmedia.indianic.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var mAuth: FirebaseAuth

    //floating button
    private var clicked = false

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_top_anim) }


    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()

        toggle = ActionBarDrawerToggle(this,drawerLayOut,R.string.open, R.string.close)
        drawerLayOut.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId)
            {

                R.id.mNumberMenu -> {
                    if (it.title.equals("Mobile Number"))
                    {
                        it.title = mAuth.currentUser!!.phoneNumber+""
                    }

                    Toast.makeText(
                        this,
                        "Mobile Number\n" + mAuth.currentUser!!.phoneNumber,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                R.id.historyMenu ->
                    Toast.makeText(this,"History", Toast.LENGTH_SHORT).show()

                R.id.logoutMenu -> {

                    val i = Intent(this, Otp::class.java)
                    startActivity(i)
                    finish()
                    Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show()

                }
            }
            true
        }


        //

        weatherViewModel.getCityData()
        initListener()

        weatherViewModel.weatherResponse.observe(this, Observer {response->

            if(response.weather[0].description == "clear sky" || response.weather[0].description == "mist"){
                Glide.with(this)
                    .load(R.drawable.clouds)
                    .into(binding.image)
            }else
                if(response.weather[0].description == "haze" || response.weather[0].description == "overcast clouds" || response.weather[0].description == "fog" ){
                    Glide.with(this)
                        .load(R.drawable.haze)
                        .into(binding.image)
                }else
                    if(response.weather[0].description == "rain"){
                        Glide.with(this)
                            .load(R.drawable.rain)
                            .into(binding.image)
                    }
            binding.description.text=response.weather[0].description
            binding.name.text=response.name
            binding.degree.text=response.wind.deg.toString()
            binding.speed.text=response.wind.speed.toString()
            binding.temp.text=response.main.temp.toString()
            binding.humidity.text=response.main.humidity.toString()

        })

        //floating button
        fb.setOnClickListener {

            fbCLicked()

        }

        fb2.setOnClickListener {

            startActivity(Intent(this,MapsActivity::class.java))
            Toast.makeText(this,"map",Toast.LENGTH_SHORT).show()

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       if (toggle.onOptionsItemSelected(item))
       {
           return true
       }

        return super.onOptionsItemSelected(item)


    }


    @ExperimentalCoroutinesApi
    private fun initListener()
    {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { weatherViewModel.setSearchQuery(it) }
                Log.d("main", "onQueryTextChange: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }

        })
    }


    //floating btn
    private fun fbCLicked() {

        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)

        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {

        if (!clicked)
        {
            fb2.visibility = View.VISIBLE
        }
        else
        {
            fb2.visibility = View.VISIBLE
        }

    }

    private fun setVisibility(clicked: Boolean) {

        if (!clicked)
        {
            fb2.startAnimation(fromBottom)
            fb.startAnimation(rotateOpen)
        }
        else
        {
            fb2.startAnimation(toBottom)
            fb.startAnimation(rotateClose)
        }

    }

    private fun setClickable(clicked: Boolean)
    {
        if (!clicked)
        {
            fb2.isClickable = true
        }
        else
        {
            fb2.isClickable = false
        }
    }
}