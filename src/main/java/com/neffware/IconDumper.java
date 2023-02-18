package com.neffware;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.core.Instance;

import java.io.File;
import java.nio.file.Path;

@ScriptManifest(category = Category.MISC, name = "com.neffware.IconDumper", author = "Neffarion", version = 1.0)
public final class IconDumper extends AbstractScript {

    private static final String DEFAULT_FOLDER = "items-icons";
    private static final Path DEFAULT_DIRECTORY = new File("").toPath().resolve(DEFAULT_FOLDER);

    @Override
    public void onStart() {
        onStart(new String[0]);
    }

    @Override
    public void onStart(String... params) {
        Dumper dumper;
        if (params == null || params.length == 0) {
            dumper = new Dumper(DEFAULT_DIRECTORY.toFile());
        } else {
            dumper = new Dumper(new File(params[0]));
        }

        while (!Instance.isCacheLoaded()) {
            Logger.log("Cache isn't loaded. Waiting...");
            Sleep.sleep(1000);
        }

        dumper.execute();
    }

    @Override
    public int onLoop() {
        return -1;
    }


}
