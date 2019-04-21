package org.xujin.moss.common;

import lombok.Builder;
import lombok.experimental.Tolerate;

/**
 * 统一的结果返回包装类型
 * @author xujin
 */
@Builder
public class ResultData {

	@Tolerate
	public ResultData(){}

	@Builder.Default
	private int code = 200;

	/** 消息Key */
	private String msgCode="200";

	/** 消息内容 */

	@Builder.Default
	private String msgContent = "success";

	/** 返回的数据 **/
	private Object data;

	/**
	 * 快捷的错误返回
	 * @param code
	 * @return
	 */
	public static ResultData.ResultDataBuilder error(int code){
		return ResultData.builder().code(code).msgContent("error");
	}

	/**
	 * 快捷的常规返回
	 * @param body
	 * @return
	 */
	public static ResultData.ResultDataBuilder ok(Object body){
		return ResultData.builder().code(200).data(body);
	}

	/**
	 * 没有内容
	 * @return
	 */
	public static ResultData.ResultDataBuilder noContent(){
		return ResultData.builder().code(204);
	}

	/**
	 * 404
	 * @return
	 */
	public static ResultData.ResultDataBuilder notFound(){
		return ResultData.builder().code(404);
	}


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
