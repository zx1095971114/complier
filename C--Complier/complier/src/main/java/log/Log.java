package log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName: complier
 * @package: log
 * @className: Log
 * @author: Zhou xiang
 * @description: 用于日志记录
 * @date: 2023/4/28 20:06
 * @version: 1.0
 */
@Slf4j
public class Log {
    private static final Logger logger = LoggerFactory.getLogger(Log.class);
    /**
     * @param info:
     * @return void
     * @author ZhouXiang
     * @description 根据错误信息记日志
     * @exception
     */
    public static void errorLog(String info, Logger logger){
        logger.error(info);
    }

//    public static void main(String[] args) {
//        logger.error("1");
//    }
}
