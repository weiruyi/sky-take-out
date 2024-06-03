package com.sky.controller.user;


import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单，参数为：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        //模拟交易成功
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);

    }

    @ApiOperation("历史订单查询")
    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("历史订单查询");
        PageResult pageResult = orderService.getHistoryOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("查询订单详情")
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetailById(@PathVariable Long id){
        log.info("查询订单详情,id={]",id);
        OrderVO orderVO = orderService.getDetailsById(id);
        return Result.success(orderVO);
    }


    @ApiOperation("取消订单")
    @PutMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable Long id){
        log.info("取消订单");
        orderService.cancelById(id);
        return Result.success();
    }

    @ApiOperation("再来一单")
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable Long id){
        log.info("再来一单");
        orderService.repetition(id);
        return Result.success();
    }

}
