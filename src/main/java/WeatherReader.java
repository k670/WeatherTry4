import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.nio.file.StandardOpenOption;

public class WeatherReader implements Runnable{
    ConcurrentLinkedQueue<org.jsoup.nodes.Element> list;
    String fileName;
    WeatherReader(ConcurrentLinkedQueue<Element> list, String fileName){
        this.list = list;
        this.fileName = fileName;
    }

    public synchronized void run() {


            Element s = list.poll();
            if (s==null){
                return;
            }else {
                String data = "\n"+s.select("a").attr("title");


                Document doc = null;
                try {
                    doc = Jsoup.connect("http://weather.bigmir.net"+s.attr("href")).timeout(1000*60*50).get();
                    //System.out.println(doc.title());
                    Date dt = new Date();
                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    Elements newsHeadlines = doc.select("td.text_center");
                    for (int i=0;i<newsHeadlines.size()-2;i++) {
                        String sEl = newsHeadlines.get(i).select("h2").toString().replace("<h2>","").replace("</h2>","").replace("</span>","");
                        data+="\n Data: "+sdf.format(c.getTime())+"\tday = "+sEl.substring(0,sEl.indexOf('&'))+"\tnight = "+sEl.substring(sEl.indexOf('>')+1)+"\t";
                        c.add(Calendar.DATE, 1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(data);
                try {
                   Singleton.getInstance().writeToFile(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        System.out.println("\nThread "+Thread.currentThread().getName()+" DONE!!!!!!!\n");
        }


}




