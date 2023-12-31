package yoon.capstone.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import yoon.capstone.application.domain.Members;
import yoon.capstone.application.domain.Orders;
import yoon.capstone.application.domain.Payment;
import yoon.capstone.application.domain.Projects;
import yoon.capstone.application.repository.OrderRepository;
import yoon.capstone.application.repository.PaymentRepository;
import yoon.capstone.application.repository.ProjectsRepository;
import yoon.capstone.application.vo.request.OrderDto;
import yoon.capstone.application.vo.response.KakaoPayResponse;
import yoon.capstone.application.vo.response.KakaoResultResponse;
import yoon.capstone.application.vo.response.OrderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${kakao.pay.key}")
    private String admin_key;

    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    private final ProjectsRepository projectsRepository;

    private Members members;

    private Projects projects;
    private Payment payment;
    private String message;

    private OrderResponse toResponse(Orders orders){
        return new OrderResponse(orders.getMembers().getUsername(), orders.getMembers().getProfile(),
                orders.getPayment().getTotal(), orders.getMessage(), orders.getPayment().getRegdate());
    }

    public List<OrderResponse> getOrderList(long idx){
        Projects tempProject = projectsRepository.findProjectsByIdx(idx);
        List<Orders> list = orderRepository.findAllByProjects(tempProject);
        List<OrderResponse> result = new ArrayList<>();
        for(Orders o:list){
            result.add(toResponse(o));
        }
        return result;
    }

    public KakaoPayResponse kakaoPayment(OrderDto dto){

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        String orderId = UUID.randomUUID().toString();
        Members members = (Members) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Projects projects = projectsRepository.findProjectsByIdx(dto.getProjectIdx());

        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", "KakaoAK " + admin_key);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("cid", "TC0ONETIME");
        map.add("partner_order_id", orderId);
        map.add("partner_user_id", members.getUsername());
        map.add("item_name", projects.getTitle());
        map.add("quantity", 1);
        map.add("total_amount", dto.getTotal());
        map.add("tax_free_amount", dto.getTotal());
        map.add("approval_url", "http://13.209.154.183:8080/api/v1/payment/success");
        map.add("cancel_url", "http://13.209.154.183:8080/api/v1/payment/cancel");
        map.add("fail_url", "http://13.209.154.183:8080/api/v1/payment/failure");

        HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(map, headers);
        KakaoPayResponse result = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                request,
                KakaoPayResponse.class
        );

        Payment payment = Payment.builder()
                .orderId(orderId)
                .members(members)
                .itemName(projects.getTitle())
                .quantity(1)
                .total(dto.getTotal())
                .tid(result.getTid())
                .regdate(result.getCreated_at())
                .build();

        this.members = members;
        this.payment = payment;
        this.projects = projects;
        this.message = dto.getMessage();

        return result;
    }

    public long kakaoPaymentAccess(String token){

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", "KakaoAK " + admin_key);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        map.add("cid","TC0ONETIME");
        map.add("tid", payment.getTid());
        map.add("partner_order_id", payment.getOrderId());
        map.add("partner_user_id", payment.getMembers().getUsername());
        map.add("pg_token", token);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

        KakaoResultResponse result = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                request,
                KakaoResultResponse.class
        );

        projects.setCurr(projects.getCurr() + payment.getTotal());
        projects.setCount(projects.getCount()+1);

        projectsRepository.save(projects);
        paymentRepository.save(payment);
        orderRepository.save(Orders.builder()
                .members(members)
                .projects(projects)
                .payment(payment)
                .message(message)
                .build());

        return projects.getIdx();
    }


}
