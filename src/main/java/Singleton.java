import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Singleton {
   private static final Singleton inst= new Singleton();

   private Singleton() {
       super();
   }

   public synchronized void writeToFile(String data) throws IOException {
       Files.write(Paths.get("D://test.txt"), data.getBytes(), StandardOpenOption.APPEND);
   }

   public static Singleton getInstance() {
       return inst;
   }

}
