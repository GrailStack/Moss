package org.xujin.moss.common.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujin
 * @param <T>
 */
@Getter
@Setter
@Builder
public class PageResult<T> {

	/**
	 * 分页的数据
	 */
	private List<T> list;

	/**
	 * 总的记录数
	 */
	private long totalCount;

	/**
	 * 总页数
	 */
	private long totalPage;

	/**
	 * 当前页数
	 */
	private long currentPage;

	public static <T> PageResultBuilder builder(List<T> list){
		return new PageResultBuilder().list(list);
	}

	public PageResult(List<T> list, long totalCount, long totalPage, long currentPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
		this.currentPage = currentPage;
	}

	public PageResult() {
	}
}
