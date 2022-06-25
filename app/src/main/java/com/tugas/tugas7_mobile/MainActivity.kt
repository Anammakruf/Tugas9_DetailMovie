package com.tugas.tugas7_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var movies: List<Movie>? = null
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_movies_list.layoutManager = LinearLayoutManager(this)
        rv_movies_list.setHasFixedSize(true)
        getMovieData { movies : List<Movie> -> rv_movies_list.adapter = MovieAdapter(movies, object : MovieAdapter.OnAdapterListener {
            override fun onClick(result: Movie) { val intent = Intent(applicationContext, DetailMovieActivity::class.java)
                intent.putExtra(DetailMovieActivity.EXTRA_DATA, result)
                startActivity(intent)
            }
        })
        }
    }
    private fun setupRecyclerView(){
        movieAdapter = MovieAdapter(arrayListOf(), object : MovieAdapter.OnAdapterListener {
            override fun onClick(result: Movie) { val intent = Intent(applicationContext, DetailMovieActivity::class.java)
                intent.putExtra(DetailMovieActivity.EXTRA_DATA, result)
                startActivity(intent)
            }
        })
        rv_movies_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
        }
    }
    private fun getMovieData(callback: (List<Movie>) -> Unit){
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                movies = response.body()!!.movies

                return callback(response.body()!!.movies)

            }
        })
    }
}
