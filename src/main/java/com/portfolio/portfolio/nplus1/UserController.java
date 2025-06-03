package com.portfolio.portfolio.nplus1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 5. 테스트용 컨트롤러
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 기존 엔드포인트 (호환성)
    @GetMapping("/n-plus-one-problem")
    public ResponseEntity<String> demonstrateNPlusOne() {
        try {
            userService.demonstrateNPlusOneProblem();
            return ResponseEntity.ok("N+1 문제 확인 완료 - 콘솔 로그를 확인하세요");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    @GetMapping("/solution")
    public ResponseEntity<String> demonstrateSolution() {
        try {
            userService.demonstrateFetchJoinSolution();
            return ResponseEntity.ok("Fetch Join 해결 방법 확인 완료 - 콘솔 로그를 확인하세요");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    @GetMapping("/fetch-join")
    public ResponseEntity<String> demonstrateFetchJoin() {
        try {
            userService.demonstrateFetchJoinSolution();
            return ResponseEntity.ok("Fetch Join 방법 확인 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    @GetMapping("/entity-graph")
    public ResponseEntity<String> demonstrateEntityGraph() {
        try {
            userService.demonstrateEntityGraphSolution();
            return ResponseEntity.ok("EntityGraph 방법 확인 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    @GetMapping("/batch-size")
    public ResponseEntity<String> demonstrateBatchSize() {
        try {
            userService.demonstrateBatchSizeSolution();
            return ResponseEntity.ok("Batch Size 방법 확인 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    // 🔥 모든 방법 한번에 비교
    @GetMapping("/compare-all")
    public ResponseEntity<String> compareAllSolutions() {
        try {
            userService.compareAllSolutions();
            return ResponseEntity.ok("모든 해결 방법 비교 완료 - 콘솔에서 SQL 쿼리 차이를 확인하세요!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }
}