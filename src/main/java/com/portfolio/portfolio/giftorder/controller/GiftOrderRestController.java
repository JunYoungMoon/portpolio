package com.portfolio.portfolio.giftorder.controller;

import com.portfolio.portfolio.giftorder.dto.CartUpdateRequest;
import com.portfolio.portfolio.giftorder.entity.Cart;
import com.portfolio.portfolio.giftorder.entity.Customer;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GiftOrderRestController {

    // 공통 메서드: 세션에서 장바구니 가져오기
    private Cart getCartFromSession(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // 공통 메서드: 고객 인증 확인
    private Customer getAuthenticatedCustomer(HttpSession session) {
        return (Customer) session.getAttribute("customer");
    }

    // 공통 메서드: 성공 응답 생성
    private ResponseEntity<Map<String, Object>> createSuccessResponse(Cart cart, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("cart", cart);
        response.put("totalItems", cart.getGalbiQuantity() + cart.getSpamQuantity());
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    // 공통 메서드: 에러 응답 생성
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 장바구니 상태 조회
     * GET /api/cart/status
     */
    @GetMapping("/cart/status")
    public ResponseEntity<Cart> getCartStatus(HttpSession session) {
        Customer customer = getAuthenticatedCustomer(session);
        if (customer == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Cart cart = getCartFromSession(session);
        return ResponseEntity.ok(cart);
    }

    /**
     * 장바구니 수량 수정 (추가/차감)
     * POST /api/cart/modify
     */
    @PostMapping("/cart/modify")
    public ResponseEntity<Map<String, Object>> modifyCart(
            @RequestParam int galbiQuantity,
            @RequestParam int spamQuantity,
            @RequestParam String action,
            HttpSession session) {

        Customer customer = getAuthenticatedCustomer(session);
        if (customer == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        // 입력값 유효성 검증
        if (galbiQuantity < 0 || spamQuantity < 0) {
            return createErrorResponse("수량은 0 이상이어야 합니다.");
        }

        if (!"add".equals(action) && !"subtract".equals(action)) {
            return createErrorResponse("잘못된 액션입니다.");
        }

        Cart cart = getCartFromSession(session);

        try {
            if ("add".equals(action)) {
                int newGalbiQty = cart.getGalbiQuantity() + galbiQuantity;
                int newSpamQty = cart.getSpamQuantity() + spamQuantity;

                // 최대 수량 체크
                if (newGalbiQty > 20 || newSpamQty > 20) {
                    return createErrorResponse("최대 주문 수량은 20개입니다.");
                }

                cart.setGalbiQuantity(newGalbiQty);
                cart.setSpamQuantity(newSpamQty);

            } else { // subtract
                cart.setGalbiQuantity(Math.max(0, cart.getGalbiQuantity() - galbiQuantity));
                cart.setSpamQuantity(Math.max(0, cart.getSpamQuantity() - spamQuantity));
            }

            cart.calculateTotal();
            session.setAttribute("cart", cart);

            String actionText = "add".equals(action) ? "추가" : "제거";
            return createSuccessResponse(cart, "장바구니가 성공적으로 " + actionText + "되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "서버 오류가 발생했습니다."));
        }
    }

    /**
     * 장바구니 수량 직접 설정 (절대값으로 설정)
     * PUT /api/cart/update
     */
    @PutMapping("/cart/update")
    public ResponseEntity<Map<String, Object>> updateCart(
            @RequestBody CartUpdateRequest request,
            HttpSession session) {

        Customer customer = getAuthenticatedCustomer(session);
        if (customer == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        // 수량 유효성 검증
        if (request.getGalbi() < 0 || request.getSpam() < 0) {
            return createErrorResponse("수량은 0 이상이어야 합니다.");
        }

        if (request.getGalbi() > 20 || request.getSpam() > 20) {
            return createErrorResponse("최대 주문 수량은 20개입니다.");
        }

        Cart cart = getCartFromSession(session);

        try {
            cart.setGalbiQuantity(request.getGalbi());
            cart.setSpamQuantity(request.getSpam());
            cart.calculateTotal();
            session.setAttribute("cart", cart);

            return createSuccessResponse(cart, "장바구니가 성공적으로 업데이트되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "서버 오류가 발생했습니다."));
        }
    }

    /**
     * 장바구니 비우기
     * DELETE /api/cart/clear
     */
    @DeleteMapping("/cart/clear")
    public ResponseEntity<Map<String, Object>> clearCart(HttpSession session) {
        Customer customer = getAuthenticatedCustomer(session);
        if (customer == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        Cart cart = getCartFromSession(session);
        cart.clear();
        session.setAttribute("cart", cart);

        return createSuccessResponse(cart, "장바구니가 비워졌습니다.");
    }

    /**
     * 특정 상품 제거
     * DELETE /api/cart/remove/{product}
     */
    @DeleteMapping("/cart/remove/{product}")
    public ResponseEntity<Map<String, Object>> removeProduct(
            @PathVariable String product,
            HttpSession session) {

        Customer customer = getAuthenticatedCustomer(session);
        if (customer == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }

        if (!"galbi".equals(product) && !"spam".equals(product)) {
            return createErrorResponse("잘못된 상품입니다.");
        }

        Cart cart = getCartFromSession(session);

        try {
            if ("galbi".equals(product)) {
                cart.setGalbiQuantity(0);
            } else {
                cart.setSpamQuantity(0);
            }

            cart.calculateTotal();
            session.setAttribute("cart", cart);

            String productName = "galbi".equals(product) ? "갈비 선물세트" : "스팸 선물세트";
            return createSuccessResponse(cart, productName + "가 장바구니에서 제거되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "서버 오류가 발생했습니다."));
        }
    }
}