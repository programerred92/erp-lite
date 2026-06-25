package com.magicbox.erp_lite.persistence.mongo.repositories;

import com.magicbox.erp_lite.persistence.mongo.documents.AuditLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLogDocument, String> {
}
