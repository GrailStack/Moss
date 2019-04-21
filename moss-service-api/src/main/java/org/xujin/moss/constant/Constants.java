package org.xujin.moss.constant;

/**
 * 常量类
 * @author xujin
 *
 */
public interface Constants {

	byte IS_DELETE_TRUE = 1;

	byte IS_DELETE_FALSE = 0;

	int CODE_ERR=100;

	//其它人的应用
	int   ATTACH_TYPE_OTHER=0;
	//我的应用
	int   ATTACH_TYPE_ME=1;
	//我收藏的应用
	int   ATTACH_TYPE_COLLECT=2;

	int TAKE_OVER_TRUE=1;

	int TAKE_OVER_FALSE=0;

	String IS_MECOLLECT_TRUE="true";

	String APP_FIND_TYPE_MY="1";

	String APP_FIND_TYPE_COLLECT="2";

	/**
	 * 数据字典项是否禁用 1标识启用
	 */
	int DICT_DATA_STATUS_TRUE=1;

	/**
	 * 数据字典项是否禁用 0表示禁用
	 */
	int DICT_DATA_STATUS_FALSE=0;

	/**
	 * Spring Boot的版本
	 */
	String SPRING_BOOT_VERSION="springBootVersion";

	/**
	 * Spring Cloud的版本
	 */
	String  SPRING_CLOUD_VERSION="springCloudVersion";

	/**
	 * App根据实例数闪烁规则
	 */
	String APP_FLICKER_RULE="appFlickerRule";

	/**
	 * 评分规则
	 */
	String SCORING_RULES="scoringRules";

	/**
	 * 注册中心列表
	 */
	String REGISTERCENTER="registerCenter";

	int REGISTER_CENTER_ENABLE=1;

	/**
	 * 多租户显示All
	 */
	String REGISTERCENTER_ALL="All";
}
