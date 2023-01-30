package com.hxy.recipe.designpattern.proxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicProxy {

    public static void main(String[] args) {
        OrderService orderService = new OrderImpl();
        orderService.order();

        DeliveryService deliveryService = new DeliveryImpl();
        deliveryService.delivery();

        log.info("<-------------------------------------------------->");

        OrderService proxyOrderService = TimerAspect.withTimeAspect(orderService);
        proxyOrderService.order();

        DeliveryService proxyDeliveryService = TimerAspect.withTimeAspect(deliveryService);
        proxyDeliveryService.delivery();
    }

}
