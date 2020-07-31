package br.tech.mobile.movieprime.Activity

import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.tech.mobile.movieprime.R
import br.tech.mobile.movieprime.data.api.TheMovieDBClient
import br.tech.mobile.movieprime.data.api.TheMovieDBInterface
import br.tech.mobile.movieprime.data.repository.NetworkState
import br.tech.mobile.movieprime.Model.MovieDetails
import br.tech.mobile.movieprime.POSTER_BASE_URL
import br.tech.mobile.movieprime.Repository.MovieDetailsRepository
import br.tech.mobile.movieprime.ViewModel.SingleMovieViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.NumberFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)



        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository =
            MovieDetailsRepository(
                apiService
            )

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })

    }

    fun bindUI(it: MovieDetails) {
        movie_title.text = it.title
        movie_tagline.text = it.tagline

//        movie_release_date.text = it.releaseDate
//        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + " minutos"
        movie_overview.text = it.overview

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            movie_overview.justificationMode = JUSTIFICATION_MODE_INTER_WORD
            movie_tagline.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
//        movie_budget.text = formatCurrency.format(it.budget)
//        movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster);


    }


    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(
                    movieRepository,
                    movieId
                ) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}
