package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {
    private ThreadLocalService service = new ThreadLocalService();

    // 동시성문제 발생 테스트

    /**
     * [thread-B] INFO hello.advanced.trace.threadlocal.code.ThreadLocalService - 저장 name=userA -> nameStore=null
     * [Thread-4] INFO hello.advanced.trace.threadlocal.code.ThreadLocalService - 저장 name=userB -> nameStore=null
     * [thread-B] INFO hello.advanced.trace.threadlocal.code.ThreadLocalService - 조회 nameStore=userA
     * [Thread-4] INFO hello.advanced.trace.threadlocal.code.ThreadLocalService - 조회 nameStore=userB
     *
     * 2번째 line : nameStore에 userA가 저장되어 있지 않은 이유
     * -> 쓰레드의 본인의 저장소에 별도에 저장되기 때문
     */
    @Test
    void fieldError() {
        log.info("main start");


        Runnable userA = () -> {
            service.logic("userA");
        };
        Runnable userB = () -> {
            service.logic("userB");

        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadA.setName("thread-B");

        threadA.start();
        sleep(100);
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
