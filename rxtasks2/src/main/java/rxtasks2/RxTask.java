package rxtasks2;

import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public final class RxTask {
    /**
     * @param callable
     * @param <R>
     * @return
     */
    @CheckReturnValue
    @NonNull
    public static <R> Single<R> single(@NonNull final Callable<Task<R>> callable) {
        return Single.fromCallable(callable).flatMap(new Function<Task<R>,
                SingleSource<? extends R>>() {
            @Override
            public SingleSource<? extends R> apply(Task<R> task) throws Exception {
                return single(task);
            }
        });
    }

    /**
     * @param task Not accept Task<Void>, @see #completes(Task) instead
     * @param <R> Result
     * @return
     */
    @CheckReturnValue
    @NonNull
    public static <R> Single<R> single(@NonNull final Task<R> task) {
        if (task.isComplete()) {
            final Exception e = task.getException();
            if (e != null) {
                return Single.error(e);
            } else {
                return Single.just(task.getResult());
            }
        }

        return Single.create(new SingleOnSubscribe<R>() {
            @Override
            public void subscribe(@NonNull final SingleEmitter<R> emit) {
                task.addOnCompleteListener(listener(emit));
            }
        });
    }

    /**
     * @param callable
     * @param <R>
     * @return
     */
    @CheckReturnValue
    @NonNull
    public static <R> Completable completes(@NonNull final Callable<Task<R>> callable) {
        return Single.fromCallable(callable).flatMapCompletable(
                new Function<Task<R>, Completable>() {
            @Override
            public Completable apply(Task<R> task) throws Exception {
                return completes(task);
            }
        });
    }

    /**
     * @param task
     * @param <R> Usually &lt;Void&gt;
     * @return
     */
    @CheckReturnValue
    @NonNull
    public static <R> Completable completes(@NonNull final Task<R> task) {
        if (task.isComplete()) {
            final Exception e = task.getException();
            if (e != null) {
                return Completable.error(e);
            } else {
                return Completable.complete();
            }
        }

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull final CompletableEmitter emit) throws Exception {
                task.addOnCompleteListener(RxTask.<R>listener(emit));
            }
        });
    }

    /**
     * @param callable
     * @param <R>
     * @return
     */
    @CheckReturnValue
    @NonNull
    public static <R> Maybe<R> maybe(@NonNull final Callable<Task<R>> callable) {
        return Single.fromCallable(callable).flatMapMaybe(
                new Function<Task<R>, MaybeSource<? extends R>>() {
            @Override
            public MaybeSource<? extends R> apply(Task<R> task) throws Exception {
                return maybe(task);
            }
        });
    }

    /**
     * @param task
     * @param <R>
     * @return
     */
    @CheckReturnValue
    @NonNull
    public static <R> Maybe<R> maybe(@NonNull final Task<R> task) {
        if (task.isComplete()) {
            final Exception e = task.getException();
            if (e != null) {
                return Maybe.error(e);
            } else {
                return Maybe.just(task.getResult());
            }
        }

        return Maybe.create(new MaybeOnSubscribe<R>() {
            @Override
            public void subscribe(@NonNull final MaybeEmitter<R> emit) {
                task.addOnCompleteListener(listener(emit));
            }
        });
    }

    /**
     * @param emit
     * @param <R>
     * @return
     */
    @NonNull
    @CheckReturnValue
    public static <R> OnCompleteListener<R> listener(@NonNull final MaybeEmitter<R> emit) {
        return new OnCompleteListener<R>() {
            @Override
            public void onComplete(@NonNull final Task<R> task) {
                if (!emit.isDisposed()) {
                    final Exception e = task.getException();
                    if (e != null) {
                        emit.onError(e);
                    } else {
                        R result = task.getResult();
                        if (result != null) {
                            emit.onSuccess(result);
                        }
                        emit.onComplete();
                    }
                }
            }
        };
    }

    /**
     * @param emit
     * @param <R>
     * @return
     */
    @NonNull
    @CheckReturnValue
    public static <R> OnCompleteListener<R> listener(@NonNull final CompletableEmitter emit) {
        return new OnCompleteListener<R>() {
            @Override
            public void onComplete(@NonNull final Task<R> task) {
                if (!emit.isDisposed()) {
                    final Exception e = task.getException();
                    if (e != null) {
                        emit.onError(e);
                    } else {
                        emit.onComplete();
                    }
                }
            }
        };
    }

    /**
     * @param emit
     * @param <R>
     * @return
     */
    @NonNull
    @CheckReturnValue
    public static <R> OnCompleteListener<R> listener(@NonNull final SingleEmitter<R> emit) {
        return new OnCompleteListener<R>() {
            @Override
            public void onComplete(@NonNull final Task<R> task) {
                if (!emit.isDisposed()) {
                    final Exception e = task.getException();
                    if (e != null) {
                        emit.onError(e);
                    } else {
                        emit.onSuccess(task.getResult());
                    }
                }
            }
        };
    }

    ///**
    // * @param callable
    // * @param <R>
    // * @return
    // */
    //@CheckReturnValue
    //@NonNull
    //public static <R> Observable<R> observe(
    // @NonNull final Callable<Task<R>> callable, @NonNull final Action onDispose) {
    //    return Single.fromCallable(callable).flatMapObservable(
    // new Function<Task<R>, ObservableSource<? extends R>>() {
    //        @Override
    //        public ObservableSource<? extends R> apply(Task<R> task) throws Exception {
    //            return observe(task, onDispose);
    //        }
    //    });
    //}

    ///**
    // * @param task
    // * @param <R>
    // * @return
    // */
    //@CheckReturnValue
    //@NonNull
    //public static <R> Observable<R> observe(
    // @NonNull final Task<R> task, @NonNull final Action onDispose) {
    //    return Observable.create(new ObservableOnSubscribe<R>() {
    //        @Override
    //        public void subscribe(@NonNull final ObservableEmitter<R> emit) throws Exception {
    //            emit.setDisposable(Disposables.fromAction(onDispose));
    //            task.addOnCompleteListener(listener(emit));
    //        }
    //    });
    //}
}
