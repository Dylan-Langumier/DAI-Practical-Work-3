package dl.dai.heig.items;

import java.util.List;

public class ItemFilter {
  public enum FilterType {
    STATISTIC_TYPE,
    ITEM_POOL,
    ITEM_ID,
    QUALITY,
  }

  public static List<Item> filterItems(List<Item> items, FilterType filterType, Object criteria) {
    return items.stream().filter(item -> matchesCriteria(item, filterType, criteria)).toList();
  }

  private static boolean matchesCriteria(Item item, FilterType filterType, Object criteria) {
    return switch (filterType) {
      case STATISTIC_TYPE -> item.statistics().stream().anyMatch(stat -> stat.type() == criteria);
      case ITEM_POOL ->
          item.itemPools().stream()
              .anyMatch(pool -> pool == Item.ItemPool.valueOf(criteria.toString()));
      case ITEM_ID -> item.id().equalsIgnoreCase(criteria.toString());
      case QUALITY -> item.quality() == (int) criteria;
    };
  }
}
