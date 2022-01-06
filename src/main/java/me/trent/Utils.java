package me.trent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class Utils {

    public static enum Types{
        A, INPUT, BUTTON, SELECT, OPTION, DIV
    }

    public static void log(String s){
        System.out.println(s);
    }

    private static int proxyNum = 0;
    public static String nextProxy(){
        if (proxyNum-1 > App.proxies.size()){
            proxyNum = 0;
        }
        return App.proxies.get(proxyNum);
    }

    public static void testProxies(){
        InputStream is = Utils.class.getClassLoader().getResourceAsStream("proxies.txt");

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                //todo; check them prior
                if (line.startsWith("//")) continue;
                App.proxies.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewTab(WebDriver driver){
        WebElement help = findElement(driver, Types.A, "Log in");
        assert help != null;
        String n = Keys.chord(Keys.CONTROL, Keys.ENTER);
        help.sendKeys(n);
    }

    public static WebElement findElement(WebDriver driver, Types type, String toEqual){

        for (WebElement el : driver.findElements(By.tagName(type.name().toLowerCase()))) {
            if (type.equals(Types.INPUT)){
                String label = el.getAttribute("aria-label");
                if (label.equalsIgnoreCase(toEqual)){
                    return el;
                }
            }
            if (type.equals(Types.BUTTON)){
                String label = el.getText();
                if (label.equalsIgnoreCase(toEqual)){
                    return el;
                }
            }
            if (type.equals(Types.SELECT)){
                String label = el.getAttribute("title");
                if (label.equalsIgnoreCase(toEqual)){
                    return el;
                }
            }
            if (type.equals(Types.OPTION)){
                String label = el.getText();
                //log(label);
                if (label.equalsIgnoreCase(toEqual)){
                    //log("found: "+label);
                    return el;
                }
            }
            if (type.equals(Types.A)){
                //String label = el.getText()+", "+el.getTagName()+", "+el.getAttribute("title")+", "+el.getAriaRole();
                if (el.getText().equalsIgnoreCase(toEqual)){
                    return el;
                }
            }
        }
        if (type.equals(Types.DIV)){
            for (WebElement el : driver.findElements(By.className(toEqual))) {
                if (el.getText().contains("Instagram code")) {
                    return el;
                }
            }
        }
        return null;
    }

    public static List<String> loadNames() {
        try {
            List<String> names = new ArrayList<>();

            InputStream is = Utils.class.getClassLoader().getResourceAsStream("names.txt");

            try (InputStreamReader streamReader =
                         new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(streamReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    names.add(line);
                }
            }
            return names;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static int currentName = 0;
    public static String getNextName(){

        if ((currentName-1) > App.names.size()){
            log("Names have been used up... resetting?");
            return null;
        }

        String name = App.names.get(currentName);
        currentName++;
        return name;
    }






    public static String getWebsiteData(String url) throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {

                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

               // Elements elements = doc.body().select("*");
                Elements elements = doc.select("*");


                for (Element element : elements) {
                    log(element.text());
                    if (element.ownText().equalsIgnoreCase("Other")){
                        continue;
                    }

                    if (element.ownText().equalsIgnoreCase("")){
                        continue;
                    }
                }
                return "NO";
            }
        });

// Block and get the result of the Future
        String result = future.get();
        System.out.println(result);
        return future.get();
    }
}
