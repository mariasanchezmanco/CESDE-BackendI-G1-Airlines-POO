package com.airlinespoo.customers.repository;

import com.airlinespoo.customers.model.DocumentType;

import java.util.List;

public interface IDocumentRepository {
    void crear(DocumentType documentType);
    DocumentType buscarPorId(String id);
    List<DocumentType> ListarTodos();
    void actualizar(DocumentType documentType);
    void eliminar(String id);
}
