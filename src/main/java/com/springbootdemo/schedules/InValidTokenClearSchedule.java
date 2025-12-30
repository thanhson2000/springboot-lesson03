package com.springbootdemo.schedules;

import com.springbootdemo.repository.InValidTokenRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
@EnableScheduling
public class InValidTokenClearSchedule {
    private static final Logger log = LoggerFactory.getLogger(InValidTokenClearSchedule.class);

    @Autowired
    private InValidTokenRepository inValidTokenRepository;

    @Transactional
    @Scheduled(cron = "0 40 20 * * ?")
    public void clearExpiredToken(){
        log.info("---開始清理無效token");

        Date now = new Date();
        int countInValidToken = inValidTokenRepository.countInValidToken(now);

        if (countInValidToken == 0){
            log.info("沒有無效token需要清理");
            return;
        }

        int clearExpiredToken = inValidTokenRepository.clearExpiredToken(now);
        log.info("清理完成！成功刪除 {} 個過期token", clearExpiredToken);
        log.info("========================================");
    }
}
