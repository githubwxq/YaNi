package com.handsome.didi.Controller;

import com.handsome.didi.Base.BaseController;
import com.handsome.didi.Bean.Banner;
import com.handsome.didi.Bean.Order;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * =====作者=====
 * 许英俊
 * =====时间=====
 * 2017/2/1.
 */
public class OrderController extends BaseController {

    public static OrderController orderController;

    public static OrderController getInstance() {
        if (orderController == null) {
            synchronized (OrderController.class) {
                if (orderController == null) {
                    orderController = new OrderController();
                }
            }
        }
        return orderController;
    }

    /**
     * 查询订单
     *
     * @param listener
     */
    public void query(final OnBmobListener listener) {
        BmobQuery<Order> query = new BmobQuery<>();
        query.setCachePolicy(mPolicy);
        query.setLimit(10);
        query.order("id");
        query.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e != null) {
                    listener.onError("error code:" + e.getErrorCode());
                    return;
                }
                if (list.isEmpty()) {
                    listener.onError("list is empty");
                    return;
                }
                if (listener != null) {
                    listener.onSuccess(list);
                }
            }
        });
    }


}