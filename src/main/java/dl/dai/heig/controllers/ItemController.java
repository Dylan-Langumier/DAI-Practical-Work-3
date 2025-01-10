package dl.dai.heig.controllers;

import dl.dai.heig.items.Item;
import dl.dai.heig.items.ItemFilter;
import io.javalin.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ItemController {
    private final ConcurrentHashMap<String, Item> items;

    public ItemController(ConcurrentHashMap<String, Item> items) {
        this.items = items;
    }

    public void getOne(Context ctx) {
        String id = ctx.pathParam("id");
        Item item = items.get(id);

        if (item == null) {
            throw new NotFoundResponse();
        }

        ctx.json(item);
    }

    public void getRandomOne(Context ctx) {
        Random rand = new Random();
        int d6 = rand.nextInt(items.size());
        List<String> keysAsArray = new ArrayList<>(items.keySet());

        ctx.json(items.get(keysAsArray.get(d6)));
    }

    public void getRandomOneByPool(Context ctx) {
        String pool = ctx.pathParam("pool");
        Random rand = new Random();
        int d6 = rand.nextInt(items.size());
        Map<String, Item> filtered = ItemFilter.filterItems(items, ItemFilter.FilterType.ITEM_POOL, pool);
        List<String> keysAsArray = new ArrayList<>(filtered.keySet());

        ctx.json(filtered.get(keysAsArray.get(d6)));
    }

    public void getMany(Context ctx) {
        ctx.json(items.values());
    }

    public void filterByQuality(Context ctx) {
        Integer quality = ctx.pathParamAsClass("quality", Integer.class).get();

        Map<String, Item> filter = ItemFilter.filterItems(items, ItemFilter.FilterType.QUALITY, quality);
        ctx.json(filter);
    }

    public void filterByPool(Context ctx) {
        String pool = ctx.pathParam("pool");
        Map<String,Item> filter = ItemFilter.filterItems(items,ItemFilter.FilterType.ITEM_POOL,pool);
        ctx.json(filter);
    }

    public void create(Context ctx) {
        Item createItem =
            ctx.bodyValidator(Item.class)
                    .check(obj -> obj.id != null, "Missing id")
                    .check(obj -> obj.name != null, "Missing name")
                    .check(obj -> obj.type != null, "Missing type")
                    .check(obj -> obj.statistics != null, "Missing statistics")
                    .check(obj -> obj.itemPools != null, "Missing itemPools")
                    .check(obj -> obj.quality != null, "Missing quality")
                    .check(obj -> obj.gameVersions != null, "Missing gameVersions")
                    .check(obj -> obj.note != null, "Missing note")
                    .get();
        Item item = new Item();
        item.id = createItem.id;
        item.name = createItem.name;
        item.type = createItem.type;
        item.statistics = createItem.statistics;
        item.itemPools = createItem.itemPools;
        item.quality = createItem.quality;
        item.gameVersions = createItem.gameVersions;
        item.note = createItem.note;
        items.put(item.id, item);

        ctx.status(HttpStatus.CREATED);
        ctx.json(item);
    }

    public void update(Context ctx){
        String id = ctx.pathParam("id");
        Item updateItem =
                ctx.bodyValidator(Item.class)
                        .check(obj -> obj.name != null, "Missing name")
                        .check(obj -> obj.type != null, "Missing type")
                        .check(obj -> obj.statistics != null, "Missing statistics")
                        .check(obj -> obj.itemPools != null, "Missing itemPools")
                        .check(obj -> obj.quality != null, "Missing quality")
                        .check(obj -> obj.gameVersions != null, "Missing gameVersions")
                        .check(obj -> obj.note != null, "Missing note")
                        .get();
        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundResponse();
        }
        item.name = updateItem.name;
        item.type = updateItem.type;
        item.statistics = updateItem.statistics;
        item.quality = updateItem.quality;
        item.gameVersions = updateItem.gameVersions;
        item.note = updateItem.note;
        items.put(id, item);
        ctx.json(item);
    }

    public void delete(Context ctx) {
        String id = ctx.pathParam("id");
        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundResponse();
        }
        items.remove(id);
        ctx.status(HttpStatus.NO_CONTENT);
    }
}
