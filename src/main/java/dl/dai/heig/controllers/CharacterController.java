package dl.dai.heig.controllers;

import dl.dai.heig.characters.Character;
import io.javalin.http.*;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class CharacterController {
  private final ConcurrentHashMap<Integer, Character> characters;
  private final ConcurrentHashMap<Integer, LocalDateTime> charactersCache;

  // This is a magic number used to store the characters' list last modification date
  // As the ID for items start at 1, it is safe to reserve the value -1 for all characters
  private final Integer RESERVED_ID_TO_IDENTIFY_ALL_CHARACTERS = -1;

  public CharacterController(
      ConcurrentHashMap<Integer, Character> characters,
      ConcurrentHashMap<Integer, LocalDateTime> charactersCache) {
    this.characters = characters;
    this.charactersCache = charactersCache;
  }

  public void getOne(Context ctx) {
    Integer id = ctx.pathParamAsClass("id", Integer.class).get();
    // Get the last known modification date of the item
    LocalDateTime lastKnownModification =
        ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);
    // Check if the item has been modified since the last known modification date
    if (lastKnownModification != null && charactersCache.get(id).equals(lastKnownModification)) {
      throw new NotModifiedResponse();
    }
    Character character = characters.get(id);

    if (character == null) {
      throw new NotFoundResponse();
    }

    LocalDateTime now;
    if (charactersCache.containsKey(Integer.valueOf(character.id))) {
      // If it is already in the cache, get the last modification date
      now = charactersCache.get(Integer.valueOf(character.id));
    } else {
      // Otherwise, set to the current date
      now = LocalDateTime.now();
      charactersCache.put(Integer.valueOf(character.id), now);
    }
    // Add the last modification date to the response
    ctx.header("Last-Modified", String.valueOf(now));
    ctx.json(character);
  }

  public void getMany(Context ctx) {
    // Get the last known modification date of all items
    LocalDateTime lastKnownModification =
        ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);
    // Check if all items have been modified since the last known modification date
    if (lastKnownModification != null
        && charactersCache.containsKey(RESERVED_ID_TO_IDENTIFY_ALL_CHARACTERS)
        && charactersCache
            .get(RESERVED_ID_TO_IDENTIFY_ALL_CHARACTERS)
            .equals(lastKnownModification)) {
      throw new NotModifiedResponse();
    }
    LocalDateTime now;
    if (charactersCache.containsKey(RESERVED_ID_TO_IDENTIFY_ALL_CHARACTERS)) {
      // If it is already in the cache, get the last modification date
      now = charactersCache.get(RESERVED_ID_TO_IDENTIFY_ALL_CHARACTERS);
    } else {
      // Otherwise, set to the current date
      now = LocalDateTime.now();
      charactersCache.put(RESERVED_ID_TO_IDENTIFY_ALL_CHARACTERS, now);
    }
    // Add the last modification date to the response
    ctx.header("Last-Modified", String.valueOf(now));
    ctx.json(characters.values());
  }

  public void create(Context ctx) {
    Character createCharacter =
        ctx.bodyValidator(Character.class)
            .check(obj -> obj.id != null, "Missing id")
            .check(obj -> obj.name != null, "Missing name")
            .check(obj -> obj.hearts != null, "Missing hearts")
            .check(obj -> obj.damage != null, "Missing damage")
            .check(obj -> obj.shotSpeed != null, "Missing shotSpeed")
            .check(obj -> obj.range != null, "Missing range")
            .check(obj -> obj.speed != null, "Missing speed")
            .check(obj -> obj.luck != null, "Missing luck")
            .check(obj -> obj.startingPickups != null, "Missing startingPickups")
            .check(obj -> obj.startingItems != null, "Missing startingItems")
            .get();
    Character character = new Character();
    character.id = createCharacter.id;
    character.name = createCharacter.name;
    character.hearts = createCharacter.hearts;
    character.damage = createCharacter.damage;
    character.shotSpeed = createCharacter.shotSpeed;
    character.range = createCharacter.range;
    character.speed = createCharacter.speed;
    character.luck = createCharacter.luck;
    character.startingPickups = createCharacter.startingPickups;
    character.startingItems = createCharacter.startingItems;
    characters.put(Integer.valueOf(character.id), character);

    if (characters.containsKey(character.id)) {
      throw new ConflictResponse();
    }

    // Store the last modification date of the item
    LocalDateTime now = LocalDateTime.now();
    charactersCache.put(Integer.valueOf(character.id), now);

    // Invalidate the cache for all items
    charactersCache.remove(RESERVED_ID_TO_IDENTIFY_ALL_CHARACTERS);

    ctx.status(HttpStatus.CREATED);

    // Add the last modification date to the response
    ctx.header("Last-Modified", String.valueOf(now));

    ctx.json(character);
  }

  public void update(Context ctx) {
    Integer id = ctx.pathParamAsClass("id", Integer.class).get();
    // Get the last known modification date of the item
    LocalDateTime lastKnownModification =
        ctx.headerAsClass("If-Unmodified-Since", LocalDateTime.class).getOrDefault(null);
    // Check if the item has been modified since the last known modification date
    if (lastKnownModification != null && !charactersCache.get(id).equals(lastKnownModification)) {
      throw new PreconditionFailedResponse();
    }

    Character updateCharacter =
        ctx.bodyValidator(Character.class)
            .check(obj -> obj.id != null, "Missing id")
            .check(obj -> obj.name != null, "Missing name")
            .check(obj -> obj.hearts != null, "Missing hearts")
            .check(obj -> obj.damage != null, "Missing damage")
            .check(obj -> obj.shotSpeed != null, "Missing shotSpeed")
            .check(obj -> obj.range != null, "Missing range")
            .check(obj -> obj.speed != null, "Missing speed")
            .check(obj -> obj.luck != null, "Missing luck")
            .check(obj -> obj.startingPickups != null, "Missing startingPickups")
            .check(obj -> obj.startingItems != null, "Missing startingItems")
            .get();
    Character character = characters.get(id);
    if (character == null) {
      throw new NotFoundResponse();
    }
    character.name = updateCharacter.name;
    character.hearts = updateCharacter.hearts;
    character.damage = updateCharacter.damage;
    character.shotSpeed = updateCharacter.shotSpeed;
    character.range = updateCharacter.range;
    character.speed = updateCharacter.speed;
    character.luck = updateCharacter.luck;
    character.startingPickups = updateCharacter.startingPickups;
    character.startingItems = updateCharacter.startingItems;
    characters.put(id, character);

    LocalDateTime now;
    if (charactersCache.containsKey(Integer.valueOf(character.id))) {
      // If it is already in the cache, get the last modification date
      now = charactersCache.get(Integer.valueOf(character.id));
    } else {
      // Otherwise, set to the current date
      now = LocalDateTime.now();
      charactersCache.put(Integer.valueOf(character.id), now);

      // Invalidate the cache for all items
      charactersCache.remove(RESERVED_ID_TO_IDENTIFY_ALL_CHARACTERS);
    }
    // Add the last modification date to the response
    ctx.header("Last-Modified", String.valueOf(now));
    ctx.json(character);
  }

  public void delete(Context ctx) {
    Integer id = ctx.pathParamAsClass("id", Integer.class).get();
    // Get the last known modification date of the item
    LocalDateTime lastKnownModification =
        ctx.headerAsClass("If-Unmodified-Since", LocalDateTime.class).getOrDefault(null);
    // Check if the item has been modified since the last known modification date
    if (lastKnownModification != null && !charactersCache.get(id).equals(lastKnownModification)) {
      throw new PreconditionFailedResponse();
    }

    Character character = characters.get(id);
    if (character == null) {
      throw new NotFoundResponse();
    }
    characters.remove(id);
    // Invalidate the cache for the item
    charactersCache.remove(id);
    // Invalidate the cache for all items
    charactersCache.remove(RESERVED_ID_TO_IDENTIFY_ALL_CHARACTERS);

    ctx.status(HttpStatus.NO_CONTENT);
  }
}
