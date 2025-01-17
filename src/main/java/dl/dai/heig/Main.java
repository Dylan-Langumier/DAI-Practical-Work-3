package dl.dai.heig;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dl.dai.heig.characters.Character;
import dl.dai.heig.controllers.CharacterController;
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
    final String filenameItems = "src/main/resources/items.json";
    final String filenameCharacters = "src/main/resources/characters.json";

    Type mapTypeItem = new TypeToken<ConcurrentHashMap<String, Item>>() {}.getType(); // Provided by ChatGPT because of reflection
    Type mapTypeCharacter = new TypeToken<ConcurrentHashMap<Integer, Character>>() {}.getType();
    Javalin app = Javalin.create(config -> {config.validation.register(LocalDateTime.class, LocalDateTime::parse);});
    Gson gson = new Gson();

    try (FileReader fileItem = new FileReader(filenameItems);
         JsonReader readerItem = new JsonReader(fileItem);
         FileReader fileCharacter = new FileReader(filenameCharacters);
         JsonReader readerCharacter = new JsonReader(fileCharacter)
        ) {
      ConcurrentHashMap<String, Item> items = gson.fromJson(readerItem, mapTypeItem);
      ConcurrentHashMap<String, LocalDateTime> itemsCache = new ConcurrentHashMap<>();

      ConcurrentHashMap<Integer, Character> characters = gson.fromJson(readerCharacter, mapTypeCharacter);
      ConcurrentHashMap<Integer, LocalDateTime> charactersCache = new ConcurrentHashMap<>();

      ItemController itemController = new ItemController(items,itemsCache);
      CharacterController characterController = new CharacterController(characters,charactersCache);

      // ITEMS ENDPOINTS
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

      // CHARACTER ENDPOINTS
      app.get("/characters", characterController::getMany);
      app.get("/character/{id}", characterController::getOne);
      app.post("/characters", itemController::create);
      app.patch("/character/{id}", characterController::update);
      app.delete("/character/{id}" ,characterController::delete);

      app.start(PORT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
