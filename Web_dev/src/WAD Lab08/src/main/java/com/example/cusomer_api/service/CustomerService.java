package com.example.cusomer_api.service;

import com.example.cusomer_api.dto.CustomerRequestDTO;
import com.example.cusomer_api.dto.CustomerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.example.cusomer_api.dto.CustomerUpdateDTO;

import java.util.List;

public interface CustomerService {
    
    //List<CustomerResponseDTO> getAllCustomers();
    
    CustomerResponseDTO getCustomerById(Long id);
    
    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);
    
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO);
    
    void deleteCustomer(Long id);
    
    List<CustomerResponseDTO> searchCustomers(String keyword);
    
    List<CustomerResponseDTO> getCustomersByStatus(String status);

    List<CustomerResponseDTO> advancedSearch(String name,String email,String status);

    CustomerResponseDTO partialUpdateCustomer(Long id, CustomerUpdateDTO updateDTO);

    Page<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, Sort sortDir);
}

