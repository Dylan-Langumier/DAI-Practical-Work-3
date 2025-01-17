package dl.dai.heig;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dl.dai.heig.controllers.ItemController;
import dl.dai.heig.items.Item;
import io.javalin.Javalin;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

  public static void main(String[] args) {
    final int PORT = 8080;
    final String filename = "src/main/resources/items.json";

    Type mapType = new TypeToken<ConcurrentHashMap<String, Item>>() {}.getType(); // Provided by ChatGPT because of reflection
    Javalin app = Javalin.create();
    Gson gson = new Gson();

    try (FileReader file = new FileReader(filename);
        JsonReader reader = new JsonReader(file)) {
      ConcurrentHashMap<String, Item> items = gson.fromJson(reader, mapType);
      ConcurrentHashMap<String, LocalDateTime> itemsCache = new ConcurrentHashMap<>();
      ItemController itemController = new ItemController(items,itemsCache);

      app.get("/items", itemController::getMany);
      app.get("/items/quality/{quality}", itemController::filterByQuality);
      app.get("/items/pool/{pool}", itemController::filterByPool);
      app.get("/items/d6", itemController::getRandomOne);
      app.get("/items/d6/{pool}", itemController::getRandomOneByPool);
      app.get("/items/d6/{quality}", itemController::getRandomOneByQuality);
      app.get("/item/{id}", itemController::getOne);

      app.post("/items", itemController::create);

      app.patch("/item/{id}", itemController::update);
      app.delete("/item/{id}" ,itemController::delete);

      app.start(PORT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
