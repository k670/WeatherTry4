import org.jsoup.Jsoup;


import java.util.concurrent.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static java.util.concurrent.TimeUnit.SECONDS;

public class WeatherTry4 {
    public static void main(String[] args) {

        String baseUrl = "http://weather.bigmir.net";
        ConcurrentLinkedQueue<Element> linkList = new ConcurrentLinkedQueue<Element>();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);;

        try {

            Document doc = Jsoup.connect("http://weather.bigmir.net/ukraine/").timeout(1000*60*5).get();
            System.out.println(doc.title());
            Elements newsHeadlines = doc.select("div.fl.W_col2");
            //Elements newsHeadlines = doc.select("div.f1.w_col2");

            for (Element headline : newsHeadlines) {

                for (Element newElem:headline.select("li") ) {

                    linkList.add(newElem);
                }

                //System.out.println("key = " +headline.select("li")+"\n\n");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }finally {
            while (linkList.size()>0) {

                executorService.scheduleAtFixedRate((new WeatherReader(linkList,  "D:\\test.txt")),0,400,TimeUnit.MILLISECONDS);
            }

            executorService.shutdown();
            /*try {
                executorService.awaitTermination(60*20, SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }


        /*System.out.println("\n\n\n\npoll = "+ linkList.poll());
        System.out.println("\n\n\n\npoll = "+ linkList.poll());*/
    }
}