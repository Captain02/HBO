package io.renren.modules.test;

import org.springframework.stereotype.Service;

@Service
public class AsynService {

    public void hello(){

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("处理数据中...");
    }
}
