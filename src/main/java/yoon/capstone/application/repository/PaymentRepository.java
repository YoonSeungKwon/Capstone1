package yoon.capstone.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.capstone.application.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findPaymentByPaymentCode(String orderId);

}
