package dl.dai.heig.items;

import java.util.List;

public class Item {
  public String id;
  public String name;
  public dl.dai.heig.items.Item.Type type;
  public List<Statistic> statistics;
  public String description;
  public List<ItemPool> itemPools;
  public Integer quality;
  public List<GameVersion> gameVersions;
  public String note;

  public Item() {
  }

  public enum ItemPool {
    AngelRoom,
    BabyShop,
    BatteryBum,
    Beggar,
    BombBum,
    Boss,
    CraneGame,
    CurseRoom,
    DevilBeggar,
    DevilRoom,
    GoldenChest,
    KeyMaster,
    Library,
    MomsChest,
    OldChest,
    Planetarium,
    RedChest,
    RottenBeggar,
    SecretRoom,
    ShellGame,
    Shop,
    TreasureRoom,
    UltraSecretRoom,
    WoodenChest,
  }

  public enum GameVersion {
    Flash,
    Rebirth,
    Afterbirth,
    AfterbirthPlus,
    Repentance,
    RepentancePlus,
    All,
  }

  public enum Type {
    Active,
    Card,
    Passive,
    Pill,
    Trinket,
  }

  public Item getSpindown(List<Item> allItems) {
    int currentIdNumber = Integer.parseInt(id.substring(1));
    int spindownIdNumber = currentIdNumber - 1;

    if (spindownIdNumber < 1) {
      return null;
    }

    String spindownId = "c" + spindownIdNumber;

    return allItems.stream().filter(item -> item.id.equals(spindownId)).findFirst().orElse(null);
  }

  @Override
  public String toString() {
    return "Item{"
        + "id='"
        + id
        + "'"
        + ", name='"
        + name
        + "'"
        + ", type="
        + type
        + ", statistics="
        + statistics
        + ", description='"
        + description
        + "'"
        + ", itemPools="
        + itemPools
        + ", quality="
        + quality
        + (gameVersions.isEmpty() ? "" : ", gameVersion=" + gameVersions)
        + (note == null ? "" : ", note='" + note)
        + '}';
  }
}
