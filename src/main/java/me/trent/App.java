package me.trent;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static List<String> names = null;
    public static List<String> proxies = new ArrayList<>();

    public static void main(String... args) throws InterruptedException {
        //Utils.log("Loading Proxies...");
        //Utils.testProxies();

        names = Utils.loadNames();
        Utils.log("Loaded " + names.size()+" names!");

        Bot bot1 = new Bot("Bot1", Utils.getNextName());
        bot1.run();

    }
}