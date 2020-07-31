package br.tech.mobile.movieprime.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.tech.mobile.movieprime.data.repository.NetworkState
import br.tech.mobile.movieprime.model.MovieDetails
import br.tech.mobile.movieprime.repository.MovieDetailsRepository
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel (private val movieRepository : MovieDetailsRepository, movieId: Int)  : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }



}