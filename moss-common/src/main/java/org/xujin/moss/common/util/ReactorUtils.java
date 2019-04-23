package org.xujin.moss.common.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * reactor 数据转换类
 * @author homeant homeanter@163.com
 * @date 2019-01-08 16:45:32
 */
public class ReactorUtils {

    public static <T> Optional<List<T>> optional(Flux<T> flux){
        return flux.collectList().blockOptional();
    }

    public static <T> Optional<T> optional(Mono<T> mono){
        return mono.blockOptional();
    }
}
