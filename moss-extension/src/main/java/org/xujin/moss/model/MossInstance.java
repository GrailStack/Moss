package org.xujin.moss.model;

import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.values.*;
import lombok.Data;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.List;

@Data
public class MossInstance {

    private  InstanceId id;
    private  long version;
    @Nullable
    private  Registration registration;
    private  boolean registered;
    private  StatusInfo statusInfo;
    private  Instant statusTimestamp;
    private  Info info;
    private  List<InstanceEvent> unsavedEvents;
    private  Endpoints endpoints;
    @Nullable
    private  BuildVersion buildVersion;
    private  Tags tags;

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
     * 是否被接管
     */
    private boolean takeOver;


    public MossInstance() {
    }

    public MossInstance(InstanceId id, long version, @Nullable Registration registration, boolean registered, StatusInfo statusInfo, Instant statusTimestamp, Info info, List<InstanceEvent> unsavedEvents, Endpoints endpoints, @Nullable BuildVersion buildVersion,
                        Tags tags, String ownerName, String projectName, String projectKey, boolean takeOver) {
        this.id = id;
        this.version = version;
        this.registration = registration;
        this.registered = registered;
        this.statusInfo = statusInfo;
        this.statusTimestamp = statusTimestamp;
        this.info = info;
        this.unsavedEvents = unsavedEvents;
        this.endpoints = endpoints;
        this.buildVersion = buildVersion;
        this.tags = tags;
        this.ownerName = ownerName;
        this.projectName = projectName;
        this.projectKey = projectKey;
        this.takeOver=takeOver;
    }
}
