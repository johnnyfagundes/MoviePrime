package br.tech.mobile.movieprime.Repository

import androidx.lifecycle.LiveData
import br.tech.mobile.movieprime.data.api.TheMovieDBInterface
import br.tech.mobile.movieprime.data.repository.MovieDetailsNetworkDataSource
import br.tech.mobile.movieprime.data.repository.NetworkState
import br.tech.mobile.movieprime.Model.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService : TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }



}