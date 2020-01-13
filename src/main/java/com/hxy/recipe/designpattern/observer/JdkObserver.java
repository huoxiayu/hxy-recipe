package com.hxy.recipe.designpattern.observer;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Observable;
import java.util.Observer;

@Slf4j
public class JdkObserver {

    private static class WeatherObservable extends Observable {

        @Getter
        @Setter
        private String currentWeather = StringUtils.EMPTY;

        public void weatherChange(String currentWeather) {
            setCurrentWeather(currentWeather);
            setChanged();
            log.info("weather change to: {}", currentWeather);
            notifyObservers();
            clearChanged();
        }

    }

    private static class WeatherSubscriber implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            if (o instanceof WeatherObservable) {
                WeatherObservable weatherObservable = (WeatherObservable) o;
                log.info("recv weather update: {}", weatherObservable.getCurrentWeather());
            }

        }
    }

    public static void main(String[] args) {
        WeatherObservable weatherObservable = new WeatherObservable();
        weatherObservable.weatherChange("sunny");

        WeatherSubscriber subscriber1 = new WeatherSubscriber();
        WeatherSubscriber subscriber2 = new WeatherSubscriber();

        weatherObservable.addObserver(subscriber1);
        weatherObservable.addObserver(subscriber2);

        weatherObservable.weatherChange("windy");

        weatherObservable.deleteObserver(subscriber1);
        weatherObservable.weatherChange("rain");
    }

}
