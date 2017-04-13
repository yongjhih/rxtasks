@file:Suppress("NOTHING_TO_INLINE")

package rxtasks2

import com.google.android.gms.tasks.Task
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

inline fun <T> Task<T>.completes()
        : Completable
        = RxTask.completes { this }

inline fun <T> Task<T>.single()
        : Single<T>
        = RxTask.single { this }
