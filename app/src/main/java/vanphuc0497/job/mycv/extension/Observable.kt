package vanphuc0497.job.mycv.extension

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by Nguyen Van Phuc on 3/13/20
 */
internal fun <T> Single<T>.observeOnUiThread(): Single<T> =
    this.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
