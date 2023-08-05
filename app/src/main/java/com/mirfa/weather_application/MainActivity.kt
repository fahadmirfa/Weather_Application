package com.mirfa.weather_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.mirfa.weather_application.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 23cfaac83e03c0e1a79b94bf0569b73c
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Lahore")
        searchCity()
    }

    private fun searchCity() {
        val searchView = binding.searchview
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return true
            }

        })
    }

    private fun fetchWeatherData(cityName:String){
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(city = cityName, appid = "23cfaac83e03c0e1a79b94bf0569b73c", units = "metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
               val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windspeed = responseBody.wind.speed
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunset = responseBody.sys.sunset.toLong()
                    val sealevel = responseBody.main.pressure
                    val max_temp = responseBody.main.temp_max
                    val min_temp = responseBody.main.temp_min
                    val condition = responseBody.weather.firstOrNull()?.main?: "unknown"

                    binding.temp.text = "$temperature °C"
                    binding.weather.text = condition
                    binding.maxTemp.text = "Max Temp: $max_temp °C"
                    binding.minTemp.text = "Min Temp: $min_temp °C"
                    binding.humidity.text = "$humidity %"
                    binding.windspeed.text = "$windspeed m/s"
                    binding.sunrise.text = "${time(sunrise)}"
                    binding.sunset.text  = "${time(sunset)}"
                    binding.sea.text = "$sealevel hPa"
                    binding.condition.text= condition
                    binding.cityName.text= "$cityName"
                    binding.day.text= dayName(System.currentTimeMillis())
                    binding.date.text= date()
changeImagesAccrdingToWeatherCondition(condition)



                    Log.d( "TAG","onResponse: $temperature")


                }


             }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun changeImagesAccrdingToWeatherCondition(conditions: String) {
        when(conditions){
            "Clouds" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Partly Clouds" ->{
                binding.root.setBackgroundResource(R.drawable.partlycloud_background)
                binding.lottieAnimationView.setAnimation(R.raw.partly_cloud)
            }
           "Overcast" ->{
                binding.root.setBackgroundResource(R.drawable.overcast_background)
                binding.lottieAnimationView.setAnimation(R.raw.overcast)
            }
            "Mist" ->{
                binding.root.setBackgroundResource(R.drawable.mist_background)
                binding.lottieAnimationView.setAnimation(R.raw.mist)
            }
            "Foggy" ->{
                binding.root.setBackgroundResource(R.drawable.foggy_background)
                binding.lottieAnimationView.setAnimation(R.raw.foggy)
            }


            "Clear Sky"->{
                binding.root.setBackgroundResource(R.drawable.clearsky_background)
                binding.lottieAnimationView.setAnimation(R.raw.clearsky)
            }
            "Sunny" ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sunny)
            }
            "Clear" ->{
                binding.root.setBackgroundResource(R.drawable.clearsky_background)
                binding.lottieAnimationView.setAnimation(R.raw.clear)
            }

            "Light Rain" ->{
                binding.root.setBackgroundResource(R.drawable.lightrain_background)
                binding.lottieAnimationView.setAnimation(R.raw.lightrain)
            }
            "Drizzle" ->{
                binding.root.setBackgroundResource(R.drawable.drizzle_background)
                binding.lottieAnimationView.setAnimation(R.raw.drizzle)
            }
           "Moderate Rain"->{
                binding.root.setBackgroundResource(R.drawable.moderaterain_background)
                binding.lottieAnimationView.setAnimation(R.raw.moderaterain)
            }
            "Showers" ->{
                binding.root.setBackgroundResource(R.drawable.showers_background)
                binding.lottieAnimationView.setAnimation(R.raw.shower)
            }
           "Heavy Rain" ->{
                binding.root.setBackgroundResource(R.drawable.heavyrain_background)
                binding.lottieAnimationView.setAnimation(R.raw.heavyrain)
            }

            "Light Snow" ->{
                binding.root.setBackgroundResource(R.drawable.lightsnow_background)
                binding.lottieAnimationView.setAnimation(R.raw.lightsnow)
            }
            "Moderate Snow"->{
                binding.root.setBackgroundResource(R.drawable.moderatesnow_background)
                binding.lottieAnimationView.setAnimation(R.raw.moderatesnow)
            }
           "Heavy Snow" ->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.heavysnow)
            }
            "Blizzard" ->{
                binding.root.setBackgroundResource(R.drawable.blizzard_background)
                binding.lottieAnimationView.setAnimation(R.raw.blizzard)
            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

   private fun date() : String{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

  private  fun time(timestamp: Long) : String{
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }

    fun dayName(timestamp: Long) : String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}

