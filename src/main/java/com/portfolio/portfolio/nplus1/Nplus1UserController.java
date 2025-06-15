package com.portfolio.portfolio.nplus1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 5. 테스트용 컨트롤러
@RestController
@RequestMapping("/api/users")
public class Nplus1UserController {

    private final Nplus1UserService nplus1UserService;

    public Nplus1UserController(Nplus1UserService nplus1UserService) {
        this.nplus1UserService = nplus1UserService;
    }

    // 한 사용자의 여러 지갑 조회
    @GetMapping("/single-user-wallets")
    public ResponseEntity<String> demonstrateSingleUserWallets() {
        try {
            nplus1UserService.demonstrateSingleUserMultipleWallets();
            return ResponseEntity.ok("한 사용자의 여러 지갑 조회 완료 - N+1 문제와 무관함을 확인하세요");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    // 기존 엔드포인트 (호환성)
    @GetMapping("/n-plus-one-problem")
    public ResponseEntity<String> demonstrateNPlusOne() {
        try {
            nplus1UserService.demonstrateNPlusOneProblem();
            return ResponseEntity.ok("N+1 문제 확인 완료 - 콘솔 로그를 확인하세요");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    @GetMapping("/solution")
    public ResponseEntity<String> demonstrateSolution() {
        try {
            nplus1UserService.demonstrateFetchJoinSolution();
            return ResponseEntity.ok("Fetch Join 해결 방법 확인 완료 - 콘솔 로그를 확인하세요");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    @GetMapping("/fetch-join")
    public ResponseEntity<String> demonstrateFetchJoin() {
        try {
            nplus1UserService.demonstrateFetchJoinSolution();
            return ResponseEntity.ok("Fetch Join 방법 확인 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    @GetMapping("/entity-graph")
    public ResponseEntity<String> demonstrateEntityGraph() {
        try {
            nplus1UserService.demonstrateEntityGraphSolution();
            return ResponseEntity.ok("EntityGraph 방법 확인 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    @GetMapping("/batch-size")
    public ResponseEntity<String> demonstrateBatchSize() {
        try {
            nplus1UserService.demonstrateBatchSizeSolution();
            return ResponseEntity.ok("Batch Size 방법 확인 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }

    // 🔥 모든 방법 한번에 비교
    @GetMapping("/compare-all")
    public ResponseEntity<String> compareAllSolutions() {
        try {
            nplus1UserService.compareAllSolutions();
            return ResponseEntity.ok("모든 해결 방법 비교 완료 - 콘솔에서 SQL 쿼리 차이를 확인하세요!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.getMessage());
        }
    }
}