package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Customer;
import com.bezkoder.springjwt.models.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
