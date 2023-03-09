package com.neffware;

import org.dreambot.api.Client;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.core.Instance;

import java.io.File;
import java.nio.file.Path;

@ScriptManifest(category = Category.MISC, name = "IconDumper", author = "Neffarion", version = 1.1)
public final class IconDumper extends AbstractScript {

    private static final String DEFAULT_FOLDER = "items-icons";
    private static final Path DEFAULT_DIRECTORY = new File("").toPath().resolve(DEFAULT_FOLDER);

    private Dumper dumper;
    private boolean run = false;
    @Override
    public void onStart() {
        onStart(new String[0]);
    }

    @Override
    public void onStart(String... params) {
        if (params == null || params.length == 0) {
            dumper = new Dumper(DEFAULT_DIRECTORY.toFile());
        } else {
            dumper = new Dumper(new File(params[0]));
        }

        while (!Instance.isCacheLoaded()) {
            Logger.log("Cache isn't loaded. Waiting...");
            Sleep.sleep(1000);
        }
    }

    @Override
    public int onLoop() {
        Logger.log("Waiting for log in...");
        if(Client.isLoggedIn() && !Client.getInstance().getRandomManager().isSolving() && !run){
            run = true;
            if(dumper.execute()){
                System.exit(0);
            }
        }

        return 1000;
    }

}
