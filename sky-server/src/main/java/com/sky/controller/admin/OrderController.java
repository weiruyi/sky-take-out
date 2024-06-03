package com.sky.controller.admin;


import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "管理端订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单搜索");
        PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> getOrderStatistics(){
        log.info("各个状态的订单数量统计");
        OrderStatisticsVO orderStatisticsVO = orderService.getStatistics();
        return Result.success(orderStatisticsVO);
    }

    @ApiOperation("查询订单详细信息")
    @GetMapping("/details/{id}")
    public Result<OrderVO> getDetailsById(@PathVariable Long id){
        log.info("查询订单详细信息,id:{}", id);
        OrderVO orderVO = orderService.getDetailsById(id);
        return Result.success(orderVO);
    }

    @ApiOperation("接单")
    @PutMapping("/confirm")
    public Result cpnfirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单");
        orderService.confrim(ordersConfirmDTO);
        return Result.success();
    }

    @ApiOperation("拒单")
    @PutMapping("/rejection")
    public Result reject(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        log.info("拒单");
        orderService.reject(ordersRejectionDTO);
        return Result.success();
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("取消订单");
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    @ApiOperation("派送")
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id){
        log.info("派送订单:{}", id);
        orderService.delivery(id);
        return Result.success();
    }

    @ApiOperation("完成订单")
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id){
        log.info("完成订单");
        orderService.complete(id);
        return Result.success();
    }
}
