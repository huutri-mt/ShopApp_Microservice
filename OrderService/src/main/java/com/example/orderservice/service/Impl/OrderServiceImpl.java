package com.example.orderservice.service.Impl;

import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.dto.request.RemoveCartItemRequest;
import com.example.orderservice.dto.response.InternalCartItemResponse;
import com.example.orderservice.dto.response.InternalCartResponse;
import com.example.orderservice.dto.response.OrderItemResponse;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.exception.AppException;
import com.example.orderservice.exception.ErrorCode;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.httpClient.AddressClient;
import com.example.orderservice.repository.httpClient.CartClient;
import com.example.orderservice.repository.httpClient.ProductClient;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.utill.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Primary
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartClient cartClient;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private AddressClient addressClient;

    public String createOrder(OrderRequest request) {
        int userId = request.getUserId();

        InternalCartResponse response = cartClient.getInternalCart(userId).getBody();
        if (response == null || response.getItems().isEmpty()) {
            log.error("No items found in cart for user ID: {}", userId);
            throw new AppException(ErrorCode.CART_EMPTY);
        }
        // Kiểm tra địa chỉ có tồn tại không
        Boolean isValidAddress = addressClient.checkAddress(request.getUserId(), request.getShippingAddress()).getBody();
        if (isValidAddress == null || !isValidAddress) {
            throw new AppException(ErrorCode.INVALID_SHIPPING_ADDRESS);
        }

        // Lọc các sản phẩm được chọn trong giỏ hàng
        List<Integer> selectedProductIds = request.getSelectedItems();
        List<InternalCartItemResponse> selectedItems = response.getItems().stream()
                .filter(item -> selectedProductIds.contains(item.getProductId()))
                .toList();

        if (selectedItems.isEmpty()) {
            log.error("Selected items do not match cart items for user ID: {}", userId);
            throw new AppException(ErrorCode.INVALID_CART_SELECTION);
        }
        for (InternalCartItemResponse item : selectedItems) {
            ResponseEntity<Boolean> productCheckResponse = productClient.checkProduct(item.getProductId(), item.getQuantity());

            Boolean isAvailable = productCheckResponse != null ? productCheckResponse.getBody() : null;

            if (isAvailable == null || !isAvailable) {
                log.error("Product ID {} is not available in sufficient quantity", item.getProductId());
                throw new AppException(ErrorCode.PRODUCT_NOT_AVAILABLE);
            }
        }



        log.info("Creating order for cart ID: {}", response.getCartId());
        Order order = new Order();
        order.setUserId(userId);

        double totalPrice = selectedItems.stream()
                .mapToDouble(item -> item.getPriceAtAdded() * item.getQuantity())
                .sum();

        order.setTotalAmount(totalPrice);
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod());

        orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for(InternalCartItemResponse item : selectedItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            productClient.updateProductStock(item.getProductId(), item.getQuantity());
            orderItem.setPriceAtAdded(item.getPriceAtAdded());
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);
        cartClient.removeSelectedItemsFromCart(new RemoveCartItemRequest(
                response.getCartId(),
                selectedProductIds
        ));
        log.info("Order created successfully with ID: {}", order.getId());
        return "Order created successfully for cart ID: " + response.getCartId();
    }

    public List<OrderResponse> getOrdersByUserId() {
        int userId = SecurityUtil.getCurrentUserId();
        log.info("Retrieving orders for user ID: {}", userId);

        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            log.warn("No orders found for user ID: {}", userId);
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        log.info("Found {} orders for user ID: {}", orders.size(), userId);

        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            OrderResponse response = new OrderResponse();
            response.setId(order.getId());
            response.setTotalAmount(order.getTotalAmount());
            response.setStatus(order.getStatus());
            response.setPaymentMethod(order.getPaymentMethod());
            log.info("Processing order ID: {}", order.getId());
            // Gọi tới AddressService để lấy chi tiết địa chỉ
            response.setShippingAddress(
                    addressClient.getAddressById(order.getShippingAddress()).getBody()
            );

            // Lấy danh sách OrderItems
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            List<OrderItemResponse> orderItemResponses = new ArrayList<>();

            for (OrderItem item : orderItems) {
                String productName = productClient.getInternalProductInfo(item.getProductId()).getBody().getName();

                OrderItemResponse itemResponse = new OrderItemResponse();
                itemResponse.setQuantity(item.getQuantity());
                itemResponse.setPrice(item.getPriceAtAdded());
                itemResponse.setProductName(productName);

                orderItemResponses.add(itemResponse);
            }

            response.setOrderItems(orderItemResponses);
            orderResponses.add(response);
        }

        return orderResponses;
    }

    public String cancleOrder(int orderId) {
        //int userId = SecurityUtil.getCurrentUserId();
        log.info("Cancelling order with ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

       /* if(userId != order.getUserId()) {
            log.error("User ID {} does not have permission to cancel order ID {}", userId, orderId);
            throw new AppException(ErrorCode.ORDER_CANCELLATION_NOT_ALLOWED);
        }*/
        if (order.getStatus() != OrderStatus.PENDING) {
            log.error("Cannot cancel order with ID {} as it is not in PENDING status", orderId);
            throw new AppException(ErrorCode.ORDER_CANCELLATION_NOT_ALLOWED);
        }

        // Cập nhật trạng thái đơn hàng
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        // Hoàn lại hàng tồn kho cho các sản phẩm trong đơn hàng
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : orderItems) {
            productClient.updateProductStock(item.getProductId(), -item.getQuantity());
        }

        log.info("Order with ID {} cancelled successfully", orderId);
        return "Order cancelled successfully";
    }

}
