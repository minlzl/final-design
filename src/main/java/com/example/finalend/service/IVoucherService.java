package com.example.finalend.service;

import com.example.finalend.entity.MyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface IVoucherService {

    Boolean addVoucher(String name, Integer stock);

    Boolean seckillVoucher(Long voucherId, Long userId) ;

    void createVoucherOrder(MyOrder myOrder);
}
