package dl.dai.heig.items;

public record Statistic(Type type, String value, Item.GameVersion gameVersion) {
  public enum Type {
    Speed,
    TearRate,
    Attack,
    Range,
    ShotSpeed,
    Luck,
    DevilDealChance,
    AngelDealChance,
    PlanetariumChance,
    Health,
    TearAmount,
    TearEffect
  }

  @Override
  public String toString() {
    String gameVer = gameVersion == null ? "" : " (" + gameVersion + ")";
    return type + ": " + value + gameVer;
  }
}
