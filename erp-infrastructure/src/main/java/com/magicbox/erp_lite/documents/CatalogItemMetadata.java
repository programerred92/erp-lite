package com.magicbox.erp_lite.documents;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record CatalogItemMetadata(String icon, String color, BigDecimal fee, List<String> nextStatuses,
                                  Map<String, Object> additionalAttributes) {
}
