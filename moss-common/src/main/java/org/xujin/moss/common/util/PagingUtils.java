package org.xujin.moss.common.util;

import org.xujin.moss.common.domain.PageResult;

import java.util.List;

/**
 * 分页工具类
 * @author xujin
 */
public class PagingUtils {

    /**
     * 计算分页开始
     * @param page
     * @param size
     * @return
     */
    public static Integer getLimitStart(Integer page,Integer size){
        return (page - 1) * size;
    }

    /**
     * 计算分页结束
     * @param page
     * @param size
     * @return
     */
    public static Integer getLimitEnd(Integer page,Integer size){
        return page * size;
    }

    /**
     * 计算总页数
     * @param total
     * @param pageSize
     * @return Integer
     */
    public static Integer getTotalPage(Integer total,Integer pageSize) {
        return (total + pageSize - 1) / pageSize;
    }

    /**
     * 分页对象的buider
     * @param list
     * @param page
     * @param size
     * @param <T>
     * @return
     */
    public static  <T> PageResult.PageResultBuilder<T> pageBuider(List<T> list, Integer page, Integer size){
        Integer total = list.size();
        Integer start = getLimitStart(page,size);
        Integer end = getLimitEnd(page,size);
        if(end > total) {
            end = total;
        }
        if(list != null && list.size() > 0){
            list = list.subList(start,end);
        }
        return PageResult.builder(list)
                .currentPage(page)
                .totalCount(total).totalPage(getTotalPage(total, size));
    }
}
