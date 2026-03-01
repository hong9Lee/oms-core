package co.oms.core.domain.model;

/** 주문 상품 */
public record OrderItem(
        String goodsCode,
        String goodsName,
        Integer quantity) {}
