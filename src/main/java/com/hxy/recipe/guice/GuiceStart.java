package com.hxy.recipe.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class GuiceStart {

    static interface FooService {
        void foo();
    }

    static class FooServiceImpl implements FooService {

        @Override
        public void foo() {
            log.info("foo");
        }

    }

    interface BarService {
        void bar();
    }

    static class BarServiceImpl implements BarService {

        @Override
        public void bar() {
            log.info("bar");
        }

    }

    interface App {
        void run();
    }

    static class AppImpl implements App {

        private final FooService fooService;
        private final BarService barService;

        @Inject
        public AppImpl(FooService fooService, BarService barService) {
            this.fooService = fooService;
            this.barService = barService;
        }

        @Override
        public void run() {
            fooService.foo();
            barService.bar();
        }

    }

    static class CustomizeModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(FooService.class).to(FooServiceImpl.class);
            bind(BarService.class).to(BarServiceImpl.class);
            bind(App.class).to(AppImpl.class);
        }

    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new CustomizeModule());
        injector.getInstance(App.class).run();
    }

}

