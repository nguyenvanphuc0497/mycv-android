package vanphuc0497.job.mycv.extension

import android.view.View
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by Nguyen Van Phuc on 3/13/20
 */
internal fun <T> Single<T>.observeOnUiThread(): Single<T> =
    this.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

internal fun <T> Observable<T>.observeOnUiThread(): Observable<T> =
    this.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

internal fun <T> Single<T>.subscribeLoadingProgressView(progressView: View?): Single<T> =
    this.doOnSubscribe {
        progressView?.showIfElseGone(true)
    }.doFinally {
        progressView?.showIfElseGone(false)
    }
