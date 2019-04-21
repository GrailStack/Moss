package org.xujin.moss.model;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.BuildVersion;
import lombok.Data;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static de.codecentric.boot.admin.server.domain.values.StatusInfo.STATUS_UNKNOWN;

@Data
public class MossApplication {

    private final String name;

    @Nullable
    private BuildVersion buildVersion;

    /**
     * 应用的状态
     */
    private String status = STATUS_UNKNOWN;

    private Instant statusTimestamp = Instant.now();

    /**
     * 实例列表
     */
    private List<Instance> instances = new ArrayList<>();

    /**
     * 负责人名称
     */
    private String ownerName;

    /**
     * 所属项目名
     */
    private String projectName;

    /**
     * 所属项目Id
     */
    private String projectKey;

    /**
     * 归属类型 我的，还是收藏的
     */
    private int attachType;

    /**
     实例书里
     */
    private int instanceNum;

    /**
     * 是否被接管
     */
    private boolean takeOver;

    /**
     * 星标数
     */
    private int starsNum;

    /**
     * 是否闪烁
     */
    private boolean twinkle;

    /**
     * 注册中心来源
     */
    private String registerSource;
}
