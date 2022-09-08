package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {
    private FieldService fieldService = new FieldService();

    @Test
    void field() {
        log.info("main start");

        /**
         *          Runnable userA = new Runnable() {
         *             @Override
         *             public void run() {
         *                 fieldService.logic("userA");
         *             }
         *         }
         */
        // 위 주석코드를 아래처럼 줄일 수 있음 (자바기본)
        Runnable userA = () -> {
            fieldService.logic("userA");
        };
        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadA.setName("thread-B");

        threadA.start();
        sleep(2000); // 동시성 문제 발생X
        threadB.start();
        sleep(3000); // main thread 종료대기

        log.info("main exit");
    }

    // 동시성문제 발생 테스트
    @Test
    void fieldError() {
        log.info("main start");

        /**
         *          Runnable userA = new Runnable() {
         *             @Override
         *             public void run() {
         *                 fieldService.logic("userA");
         *             }
         *         }
         */
        // 위 주석코드를 아래처럼 줄일 수 있음 (자바기본)
        Runnable userA = () -> {
            fieldService.logic("userA");
        };
        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadA.setName("thread-B");

        threadA.start();
        sleep(100); // 동시성 문제 발생O (저장로직이 끝나기전에 다른 쓰레드가 들어와서 데이터에 접근함)
                          // 동시성 이슈는 같은 "인스턴스"의 필드에 동시에 접근하면 발생함
                          // 스프링은 기본적으로 싱글톤이기때문에 !!
                          // 지역변수에는 동시성 문제가 발생하지 않음 (지역변수는 쓰레드 마다 다른 메모리 영역에 할당되기때문)
                          // 인스턴스의 필드(주로 싱글톤), 또는 static같은 공용 필드에 접근 (조회만 하면은 발생하지않음, 값을변경해야만 일어남)
        threadB.start();
        sleep(3000); // main thread 종료대기

        log.info("main exit");
    }



    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
