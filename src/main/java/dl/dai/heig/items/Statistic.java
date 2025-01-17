package dl.dai.heig.items;

public record Statistic(Type type, String value, Item.GameVersion[] gameVersions) {
  public enum Type {
    Speed,
    Tears,
    Attack,
    Range,
    ShotSpeed,
    Luck,
    Size,
    DevilDealChance,
    AngelDealChance,
    PlanetariumChance,
    Health,
    TearAmount,
    TearEffect,
    TearRate,
    TearSize
  }

  @Override
  public String toString() {
    String gameVer = gameVersion == null ? "" : " (" + gameVersion + ")";
    return type + ": " + value + gameVer;
  }
}
