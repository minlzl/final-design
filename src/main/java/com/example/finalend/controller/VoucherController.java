package com.example.finalend.controller;

import com.example.finalend.annotation.ApiRestController;
import com.example.finalend.service.IVoucherService;
import com.example.finalend.vo.Resp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@ApiRestController
public class VoucherController {

    @Resource
    IVoucherService iVoucherService;

    @PostMapping("/voucher")
    public Resp<Object> addVoucher(@RequestParam("name") String name, @RequestParam("stock") Integer stock) {
        iVoucherService.addVoucher(name, stock);
        return Resp.success();
    }

    @PostMapping("/voucher/seckill")
    public Resp<Object> seckillVoucher(@RequestParam("voucherId") Long voucherId, @RequestParam("userId") Long userId) {
        Boolean voucher = iVoucherService.seckillVoucher(voucherId, userId);
        return Resp.success(voucher);
    }

}
