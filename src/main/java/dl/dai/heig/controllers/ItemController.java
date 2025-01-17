package dl.dai.heig.controllers;

import dl.dai.heig.items.Item;
import dl.dai.heig.items.ItemFilter;
import io.javalin.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;

public class ItemController {
    private final ConcurrentHashMap<String, Item> items;
    private final ConcurrentHashMap<String,LocalDateTime> itemsCache;

    // This is a magic number used to store the items' list last modification date
    // As the ID for items will never be All Items, it is safe to reserve the value All Items for all items
    private final String RESERVED_ID_TO_IDENTIFY_ALL_ITEMS = "All Items";

    public ItemController(ConcurrentHashMap<String, Item> items, ConcurrentHashMap<String,LocalDateTime> itemsCache) {
        this.items = items;
        this.itemsCache = itemsCache;
    }

    public void getOne(Context ctx) {
        String id = ctx.pathParam("id");
        // Get the last known modification date of the item
        LocalDateTime lastKnownModification = ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);
        // Check if the item has been modified since the last known modification date
        if (lastKnownModification != null && itemsCache.get(id).equals(lastKnownModification)) {
            throw new NotModifiedResponse();
        }
        Item item = items.get(id);

        if (item == null) {
            throw new NotFoundResponse();
        }

        LocalDateTime now;
        if(itemsCache.containsKey(item.id)){
            // If it is already in the cache, get the last modification date
            now = itemsCache.get(item.id);
        }else{
            // Otherwise, set to the current date
            now = LocalDateTime.now();
            itemsCache.put(item.id, now);
        }
        // Add the last modification date to the response
        ctx.header("Last-Modified", String.valueOf(now));
        ctx.json(item);
    }

    // Cache is not possible here, because we don't have an id to check if the user already has it in their cache.
    public void getRandomOne(Context ctx) {
        Random rand = new Random();
        int d6 = rand.nextInt(items.size());
        List<String> keysAsArray = new ArrayList<>(items.keySet());

        ctx.json(items.get(keysAsArray.get(d6)));
    }

    // Cache is not possible here, because we don't have an id to check if the user already has it in their cache.
    public void getRandomOneByPool(Context ctx) {
        String pool = ctx.pathParam("pool");
        Random rand = new Random();
        int d6 = rand.nextInt(items.size());
        Map<String, Item> filtered = ItemFilter.filterItems(items, ItemFilter.FilterType.ITEM_POOL, pool);
        List<String> keysAsArray = new ArrayList<>(filtered.keySet());

        ctx.json(filtered.get(keysAsArray.get(d6)));
    }

    public void getRandomOneByQuality(Context ctx) {
        String quality = ctx.pathParam("quality");
        Random rand = new Random();
        int d6 = rand.nextInt(items.size());
        Map<String, Item> filtered = ItemFilter.filterItems(items, ItemFilter.FilterType.QUALITY, quality);
        List<String> keysAsArray = new ArrayList<>(filtered.keySet());

        ctx.json(filtered.get(keysAsArray.get(d6)));
    }

    public void getMany(Context ctx) {
        // Get the last known modification date of all items
        LocalDateTime lastKnownModification = ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);
        // Check if all items have been modified since the last known modification date
        if (lastKnownModification != null
                && itemsCache.containsKey(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS)
                && itemsCache.get(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS).equals(lastKnownModification)){
            throw new NotModifiedResponse();
        }
        LocalDateTime now;
        if (itemsCache.containsKey(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS)) {
            // If it is already in the cache, get the last modification date
            now = itemsCache.get(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS);
        }else{
            // Otherwise, set to the current date
            now = LocalDateTime.now();
            itemsCache.put(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS, now);
        }
        // Add the last modification date to the response
        ctx.header("Last-Modified", String.valueOf(now));
        ctx.json(items.values());
    }

    public void filterByQuality(Context ctx) {

        // Get the last known modification date of all items
        LocalDateTime lastKnownModification = ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);
        // Check if all items have been modified since the last known modification date
        if (lastKnownModification != null
                && itemsCache.containsKey(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS)
                && itemsCache.get(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS).equals(lastKnownModification)){
            throw new NotModifiedResponse();
        }

        Integer quality = ctx.pathParamAsClass("quality", Integer.class).get();
        Map<String, Item> filter = ItemFilter.filterItems(items, ItemFilter.FilterType.QUALITY, quality);

        LocalDateTime now;
        if (itemsCache.containsKey(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS)) {
            // If it is already in the cache, get the last modification date
            now = itemsCache.get(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS);
        }else{
            // Otherwise, set to the current date
            now = LocalDateTime.now();
            itemsCache.put(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS, now);
        }


        ctx.json(filter);
    }

    public void filterByPool(Context ctx) {
        // Get the last known modification date of all items
        LocalDateTime lastKnownModification = ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);
        // Check if all items have been modified since the last known modification date
        if (lastKnownModification != null
                && itemsCache.containsKey(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS)
                && itemsCache.get(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS).equals(lastKnownModification)){
            throw new NotModifiedResponse();
        }

        String pool = ctx.pathParam("pool");
        Map<String,Item> filter = ItemFilter.filterItems(items,ItemFilter.FilterType.ITEM_POOL,pool);

        LocalDateTime now;
        if (itemsCache.containsKey(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS)) {
            // If it is already in the cache, get the last modification date
            now = itemsCache.get(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS);
        }else{
            // Otherwise, set to the current date
            now = LocalDateTime.now();
            itemsCache.put(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS, now);
        }

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
        if(items.containsKey(item.id)){
            throw new ConflictResponse();
        }
        items.put(item.id, item);

        // Store the last modification date of the item
        LocalDateTime now = LocalDateTime.now();
        itemsCache.put(item.id, now);

        // Invalidate the cache for all items
        itemsCache.remove(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS);

        ctx.status(HttpStatus.CREATED);

        // Add the last modification date to the response
        ctx.header("Last-Modified", String.valueOf(now));

        ctx.json(item);
    }

    public void update(Context ctx){
        String id = ctx.pathParam("id");
        // Get the last known modification date of the item
        LocalDateTime lastKnownModification = ctx.headerAsClass("If-Unmodified-Since", LocalDateTime.class).getOrDefault(null);
        // Check if the item has been modified since the last known modification date
        if (lastKnownModification != null && !itemsCache.get(id).equals(lastKnownModification)){
            throw new PreconditionFailedResponse();
        }

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

        LocalDateTime now;
        if (itemsCache.containsKey(item.id)) {
            // If it is already in the cache, get the last modification date
            now = itemsCache.get(item.id);
        }else{
            // Otherwise, set to the current date
            now = LocalDateTime.now();
            itemsCache.put(item.id, now);

            // Invalidate the cache for all items
            itemsCache.remove(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS);
        }
        // Add the last modification date to the response
        ctx.header("Last-Modified", String.valueOf(now));
        ctx.json(item);
    }

    public void delete(Context ctx) {
        String id = ctx.pathParam("id");
        // Get the last known modification date of the item
        LocalDateTime lastKnownModification = ctx.headerAsClass("If-Unmodified-Since", LocalDateTime.class).getOrDefault(null);
        // Check if the item has been modified since the last known modification date
        if (lastKnownModification != null && !itemsCache.get(id).equals(lastKnownModification)){
            throw new PreconditionFailedResponse();
        }

        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundResponse();
        }
        items.remove(id);
        // Invalidate the cache for the item
        itemsCache.remove(id);
        // Invalidate the cache for all items
        itemsCache.remove(RESERVED_ID_TO_IDENTIFY_ALL_ITEMS);

        ctx.status(HttpStatus.NO_CONTENT);
    }
}
