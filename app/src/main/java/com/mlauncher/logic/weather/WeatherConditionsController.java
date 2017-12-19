package com.mlauncher.logic.weather;

import com.mlauncher.logic.weather.model.WeatherConditions;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daba on 2016-09-02.
 */

public class WeatherConditionsController {

    public static final String IF_ADDRESS = "http://www.if.pw.edu.pl/~meteo/index-mob.php";
    public static final int INTERVAL = 6;

    public interface Listener {
        void onData(WeatherConditions weatherConditions);
    }

    private Listener listener;
    private Subscription subscription;

    public WeatherConditionsController(Listener listener) {
        this.listener = listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void schedule() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = Observable
                .just(0)
                .delay(getInterval(), TimeUnit.SECONDS)
                .repeat()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {

                        refreshWeatherConditions();
                    }
                });
    }

    public void stop() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public int getInterval() {
        return INTERVAL;
    }

    public void refreshWeatherConditions() {
        new ContentDownloader()
                .downloadObservable(IF_ADDRESS)
                .map(new Func1<String, WeatherConditions>() {
                    @Override
                    public WeatherConditions call(String s) {
                        return new ContentParser().parse(s);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<WeatherConditions>() {
                    @Override
                    public void call(WeatherConditions weatherConditions) {
                        if (listener != null) {
                            listener.onData(weatherConditions);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
}