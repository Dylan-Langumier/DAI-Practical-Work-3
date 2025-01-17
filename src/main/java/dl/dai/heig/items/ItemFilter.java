package dl.dai.heig.items;

import java.util.Map;
import java.util.stream.Collectors;

public class ItemFilter {
  public enum FilterType {
    STATISTIC_TYPE,
    ITEM_POOL,
    QUALITY,
  }

  public static Map<String, Item> filterItems(
      Map<String, Item> items, FilterType filterType, Object criteria) {
    return items.entrySet().stream()
        .filter(item -> matchesCriteria(item.getValue(), filterType, criteria))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static boolean matchesCriteria(Item item, FilterType filterType, Object criteria) {
    return switch (filterType) {
      case STATISTIC_TYPE -> item.statistics.stream().anyMatch(stat -> stat.type() == criteria);
      case ITEM_POOL ->
          item.itemPools.stream()
              .anyMatch(pool -> pool == Item.ItemPool.valueOf(criteria.toString()));
      case QUALITY -> item.quality == (int) criteria;
    };
  }
}
