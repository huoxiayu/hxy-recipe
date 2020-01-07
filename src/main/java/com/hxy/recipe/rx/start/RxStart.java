package com.hxy.recipe.rx.start;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action1;
import rx.observables.AsyncOnSubscribe;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class RxStart {

    public static void main(String[] args) {
        // basic();

        scheduler();
    }

    private static Subscriber<String> newStringScribe(long timeoutInMillis) {
        return new Subscriber<>() {
            @Override
            public void onStart() {
                log.info("subscribe start");
            }

            @Override
            public void onCompleted() {
                log.info("subscribe complete");
            }

            @Override
            public void onError(Throwable e) {
                log.error("subscribe error: {}", e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Utils.sleepInMillis(timeoutInMillis);
                log.info("subscribe msg: {}", s);
            }
        };
    }

    private static void basic() {
        Action1<String> printConsumer = s -> log.info("receive msg: {}", s);
        Action1<Throwable> errorConsumer = e -> log.error("receive error: {}", e.getMessage());

        Single.create((Single.OnSubscribe<String>) singleSubscriber -> singleSubscriber.onSuccess("some msg"))
            .subscribe(printConsumer, errorConsumer);

        Single.create((Single.OnSubscribe<String>) singleSubscriber -> singleSubscriber.onError(new RuntimeException("some error")))
            .subscribe(printConsumer, errorConsumer);

        Observable.from(List.of("1", "2", "3")).subscribe(printConsumer);

        Observable.just("x", "y", "z").subscribe(printConsumer);

        Observer<String> stringObserver = new Observer<>() {
            @Override
            public void onCompleted() {
                log.info("observer complete");
            }

            @Override
            public void onError(Throwable e) {
                log.error("observer error: {}", e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log.info("observer msg: {}", s);
            }
        };

        Observable.just("o", "p", "q").subscribe(stringObserver);

        Observable<String> observable = Observable.error(new RuntimeException("some error"));
        observable.subscribe(stringObserver);

        Subscriber<String> subscriber = newStringScribe(0L);

        Observable.just("g", "k", "h").subscribe(subscriber);

        Observable<String> errorObservable = Observable.error(new RuntimeException("some error"));
        errorObservable.subscribe(subscriber);

        Observable.create(new SyncOnSubscribe<String, String>() {
            @Override
            protected String generateState() {
                return "1";
            }

            @Override
            protected String next(String state, Observer<? super String> observer) {
                int val = Integer.parseInt(state);
                observer.onNext(String.valueOf(val));
                if (val >= 10) {
                    observer.onCompleted();
                    return "done";
                } else {
                    return String.valueOf(val + 1);
                }
            }
        }).subscribe(stringObserver);

        Observable.create(new AsyncOnSubscribe<String, String>() {
            @Override
            protected String generateState() {
                return "1";
            }

            @Override
            protected String next(String state, long requested, Observer<Observable<? extends String>> observer) {
                int val = Integer.parseInt(state);
                observer.onNext(Observable.just(String.valueOf(val)));
                if (val >= 10) {
                    observer.onCompleted();
                    return "done";
                } else {
                    return String.valueOf(val + 1);
                }
            }
        }).subscribe(stringObserver);
    }

    private static void scheduler() {
        Subscriber<String> subscriber = newStringScribe(50L);
        Observable.from(IntStream.rangeClosed(1, 10).mapToObj(String::valueOf).collect(Collectors.toList()))
            .subscribeOn(Schedulers.from(Executors.newCachedThreadPool(Utils.newThreadFactory("customize-"))))
            .map(Integer::parseInt)
            .map(i -> i * 2)
            .filter(i -> i <= 10)
            .map(String::valueOf)
            .subscribe(subscriber);

        Utils.sleep(5L);

        subscriber.unsubscribe();
    }
}
