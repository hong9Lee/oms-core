package co.oms.core.adapter.out.persistence;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 주문 상품 MongoDB 내장 Entity */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItemEntity {

    private String goodsCode;
    private String goodsName;
    private Integer quantity;
}
