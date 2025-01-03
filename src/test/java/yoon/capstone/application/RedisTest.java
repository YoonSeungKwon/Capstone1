package yoon.capstone.application;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yoon.capstone.application.common.dto.request.OrderDto;
import yoon.capstone.application.service.domain.Members;
import yoon.capstone.application.infrastructure.jpa.MemberJpaRepository;
import yoon.capstone.application.infrastructure.jpa.OrderJpaRepository;
import yoon.capstone.application.infrastructure.jpa.PaymentJpaRepository;
import yoon.capstone.application.infrastructure.jpa.ProjectsJpaRepository;
import yoon.capstone.application.service.OrderService;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class RedisTest {


    @Autowired
    MemberJpaRepository memberRepository;

    @Autowired
    ProjectsJpaRepository projectsRepository;

    @Autowired
    OrderJpaRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentJpaRepository paymentRepository;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void test() throws InterruptedException {

        int testSize = 200;
        Random random = new Random();

        AtomicInteger success = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        CountDownLatch countDownLatch = new CountDownLatch(testSize);

        for(int i=0; i<testSize; i++){
            executorService.execute(()->{
                try{
                    Members members = memberRepository.findMembersByMemberIdx(2000+random.nextInt(2000)).orElseThrow();

                    OrderDto dto = new OrderDto(506, random.nextInt(100,1000), "test");

                    String code = String.valueOf(orderService.kakaoPayment(dto));

                    System.out.println(code);
//                    orderService.kakaoPaymentAccess(code, "tokenTest");

                    success.incrementAndGet();
                }catch (Exception e){

                    failure.incrementAndGet();
                }finally {
                    countDownLatch.countDown();
                }
            });
        }



        countDownLatch.await();

        System.out.println("Success :" + success);
        System.out.println("Failure :" + failure);

    }



}
