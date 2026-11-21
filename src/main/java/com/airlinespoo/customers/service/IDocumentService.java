package com.airlinespoo.customers.service;

import com.airlinespoo.core.repository.Repository;
import com.airlinespoo.customers.model.DocumentType;

import java.util.List;

public interface IDocumentService {

        void createDocument(DocumentType documentType);
        DocumentType getDocumentById(String id);
        List<DocumentType> listDocuments();
        void updateDocument(DocumentType documentType);
        void deleteDocument(String id);
    }

