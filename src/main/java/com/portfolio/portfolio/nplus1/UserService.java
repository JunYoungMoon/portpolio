package com.portfolio.portfolio.nplus1;

import com.portfolio.portfolio.nplus1.repository.jpa.User;
import com.portfolio.portfolio.nplus1.repository.jpa.UserBalance;
import com.portfolio.portfolio.nplus1.repository.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 한 사용자의 여러 지갑 조회 (N+1 문제 없음)
    public void demonstrateSingleUserMultipleWallets() {
        System.out.println("\n=== 📍 비교: 한 사용자의 여러 지갑 조회 (N+1 문제 없음) ===");

        // 1. 첫 번째 사용자만 조회 (1번 쿼리)
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("사용자가 없습니다.");
            return;
        }

        User firstUser = users.get(0);
        System.out.println("조회된 사용자: " + firstUser.getUsername());

        // 2. 이 한 사용자의 모든 지갑 조회 (1번 쿼리)
        List<UserBalance> balances = firstUser.getUserBalances();
        System.out.println("이 사용자의 총 지갑 개수: " + balances.size());

        // 3. 각 지갑 정보 출력 (추가 쿼리 없음!)
        System.out.println("각 지갑 정보:");
        balances.forEach(balance -> {
            System.out.println("  - 지갑: " + balance.getWallet() + ", 잔액: " + balance.getTotalBalance());
            // 이미 로딩된 데이터이므로 추가 쿼리 실행 안됨
        });

        System.out.println("총 실행된 쿼리: 2번 (User 조회 1번 + UserBalance 조회 1번)");
        System.out.println("=== 한 사용자 지갑 조회 완료 (N+1 문제와 무관) ===\n");
    }

    // ❌ N+1 문제가 발생하는 메서드
    public void demonstrateNPlusOneProblem() {
        System.out.println("\n=== ❌ N+1 문제 발생 케이스 ===");

        // 1. 모든 사용자를 조회 (1번의 쿼리)
        List<User> users = userRepository.findAll();
        System.out.println("사용자 조회 완료: " + users.size() + "명");

        // 2. 각 사용자의 잔액을 조회 (N번의 추가 쿼리 발생)
        for (User user : users) {
            List<UserBalance> balances = user.getUserBalances();
            System.out.println("사용자 " + user.getUsername() + "의 잔액 개수: " + balances.size());

            // 실제로 데이터에 접근해야 lazy loading이 발생
            balances.forEach(balance ->
                    System.out.println("  - 잔액: " + balance.getTotalBalance())
            );
        }
        System.out.println("=== N+1 문제 케이스 완료 ===\n");
    }

    // ✅ 해결 방법 1: Fetch Join 사용
    public void demonstrateFetchJoinSolution() {
        System.out.println("\n=== ✅ 해결 방법 1: Fetch Join ===");

        // fetch join을 사용하여 한 번의 쿼리로 모든 데이터 조회
        List<User> users = userRepository.findAllWithBalances();
        System.out.println("사용자 조회 완료: " + users.size() + "명");

        for (User user : users) {
            List<UserBalance> balances = user.getUserBalances();
            System.out.println("사용자 " + user.getUsername() + "의 잔액 개수: " + balances.size());
            balances.forEach(balance ->
                    System.out.println("  - 잔액: " + balance.getTotalBalance())
            );
        }
        System.out.println("=== Fetch Join 해결 완료 ===\n");
    }

    // ✅ 해결 방법 2: EntityGraph 사용
    public void demonstrateEntityGraphSolution() {
        System.out.println("\n=== ✅ 해결 방법 2: EntityGraph ===");

        // @EntityGraph를 사용하여 한 번의 쿼리로 모든 데이터 조회
        List<User> users = userRepository.findAllWithEntityGraph();
        System.out.println("사용자 조회 완료: " + users.size() + "명");

        for (User user : users) {
            List<UserBalance> balances = user.getUserBalances();
            System.out.println("사용자 " + user.getUsername() + "의 잔액 개수: " + balances.size());
            balances.forEach(balance ->
                    System.out.println("  - 잔액: " + balance.getTotalBalance())
            );
        }
        System.out.println("=== EntityGraph 해결 완료 ===\n");
    }

    // ✅ 해결 방법 3: Batch Size (설정 필요)
    public void demonstrateBatchSizeSolution() {
        System.out.println("\n=== ✅ 해결 방법 3: Batch Size ===");
        System.out.println("주의: application.yml에 default_batch_fetch_size 설정 필요");

        // 일반 조회지만 batch size 설정으로 개선됨
        List<User> users = userRepository.findAll();
        System.out.println("사용자 조회 완료: " + users.size() + "명");

        for (User user : users) {
            List<UserBalance> balances = user.getUserBalances();
            System.out.println("사용자 " + user.getUsername() + "의 잔액 개수: " + balances.size());
            balances.forEach(balance ->
                    System.out.println("  - 잔액: " + balance.getTotalBalance())
            );
        }
        System.out.println("=== Batch Size 해결 완료 ===\n");
    }

    // 🔥 모든 방법을 한번에 비교
    public void compareAllSolutions() {
        System.out.println("\n🔥🔥🔥 모든 해결 방법 비교 시작 🔥🔥🔥");

        // 1. N+1 문제 발생
        demonstrateNPlusOneProblem();

        // 2. Fetch Join 해결
        demonstrateFetchJoinSolution();

        // 3. EntityGraph 해결
        demonstrateEntityGraphSolution();

        // 4. Batch Size 해결
        demonstrateBatchSizeSolution();

        System.out.println("🎉🎉🎉 모든 비교 완료! 콘솔에서 SQL 쿼리 수를 확인하세요 🎉🎉🎉");
    }

    // 기존 메서드는 호환성을 위해 유지 (Fetch Join 방식 사용)
    public void demonstrateSolution() {
        demonstrateFetchJoinSolution();
    }
}