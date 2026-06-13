package com.magicbox.erp_lite.documents;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "audit_logs")
public class AuditLogDocument {
    @Id
    private ObjectId id;
    private String className;
    private String methodName;
    private String endpoint;
    private String ipAddress;
    private String userId;
    private Boolean success;
    private String errorMessage;
    private Long executionTimeMs;
    private Instant timestamp;
}