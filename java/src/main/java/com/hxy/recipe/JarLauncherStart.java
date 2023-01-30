package com.hxy.recipe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.loader.ExecutableArchiveLauncher;
import org.springframework.boot.loader.JarLauncher;
import org.springframework.boot.loader.Launcher;
import org.springframework.boot.loader.PropertiesLauncher;
import org.springframework.boot.loader.archive.Archive;

/**
 * @see Launcher
 * @see ExecutableArchiveLauncher
 * @see JarLauncher
 * @see PropertiesLauncher
 */
@Slf4j
public class JarLauncherStart {

    private static class DelegateLauncher extends ExecutableArchiveLauncher {

        // visible for main method
        public void launch(String[] args) throws Exception {
            super.launch(args);
        }

        // annotated with @SpringBootApplication
        @Override
        protected String getMainClass() {
            return RecipeApplication.class.getName();
        }

        @Override
        protected boolean isNestedArchive(Archive.Entry entry) {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        new DelegateLauncher().launch(args);
    }

}

