package dl.dai.heig;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dl.dai.heig.items.Item;
import dl.dai.heig.items.ItemFilter;
import io.javalin.Javalin;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class Main {

  public static void main(String[] args) {
    final int PORT = 8080;
    final String filename = "src/main/resources/items.json";

    Type listType =
        new TypeToken<List<Item>>() {}.getType(); // Provided by ChatGPT because of reflection
    Javalin app = Javalin.create();
    Gson gson = new Gson();

    Random random = new Random();

    try (FileReader file = new FileReader(filename);
        JsonReader reader = new JsonReader(file)) {
      List<Item> items = gson.fromJson(reader, listType);

      app.get(
          "/items",
          ctx -> {
            StringBuilder result = new StringBuilder();
            items.forEach(item -> result.append(item).append("\n"));
            ctx.result(result.toString());
          });

      app.get(
          "/items/{quality}",
          ctx -> {
            String quality = ctx.pathParam("quality");
            StringBuilder result = new StringBuilder();
            List<Item> filteredItems =
                ItemFilter.filterItems(
                    items, ItemFilter.FilterType.QUALITY, Integer.valueOf(quality));
            if (filteredItems.isEmpty()) {
              result.append("No item with quality '").append(quality).append("'");
            } else {
              filteredItems.forEach(item -> result.append(item).append("\n"));
            }
            ctx.result(result.toString());
          });

      app.get(
          "/item/{id}",
          ctx -> {
            String id = ctx.pathParam("id");
            StringBuilder result = new StringBuilder();
            List<Item> filteredItems =
                ItemFilter.filterItems(items, ItemFilter.FilterType.ITEM_ID, id);
            if (filteredItems.isEmpty()) {
              result.append("Item '").append(id).append("' not found");
            } else {
              result.append(filteredItems.getFirst());
            }
            ctx.result(result.toString());
          });

      app.get(
          "/d6",
          ctx -> {
            StringBuilder result = new StringBuilder();
            int d6 = random.nextInt(items.size());
            result.append(items.get(d6).toString());
            ctx.result(result.toString());
          });

      app.get(
          "/d6/{itemPool}",
          ctx -> {
            String itemPool = ctx.pathParam("itemPool");
            StringBuilder result = new StringBuilder();
            List<Item> filteredItems =
                ItemFilter.filterItems(items, ItemFilter.FilterType.ITEM_POOL, itemPool);
            int d6 = random.nextInt(filteredItems.size());
            result.append(filteredItems.get(d6).toString());
            ctx.result(result.toString());
          });

      app.get(
          "/spindown/{itemId}",
          ctx -> {
            String itemId = ctx.pathParam("itemId");
            StringBuilder result = new StringBuilder();
            List<Item> filteredItems =
                ItemFilter.filterItems(items, ItemFilter.FilterType.ITEM_ID, itemId);
            if (filteredItems.isEmpty()) {
              result.append("Item '").append(itemId).append("' not found");
            } else {
              Item item = filteredItems.getFirst();
              result.append(item).append("\n\n");

              Item spunDown = item.getSpindown(items);
              result.append("Spindown: ");
              if (spunDown == null || spunDown.name().equalsIgnoreCase("TmTrainer"))
                result.append("Item cannot be spun down");
              else result.append(item.getSpindown(items));
            }
            ctx.result(result.toString());
          });

      app.start(PORT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
