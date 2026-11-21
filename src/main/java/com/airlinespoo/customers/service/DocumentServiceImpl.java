
package com.airlinespoo.customers.service;

import com.airlinespoo.core.exception.NotFoundException;
import com.airlinespoo.core.exception.DuplicateException;
import com.airlinespoo.core.exception.BusinessRuleException;

import com.airlinespoo.customers.model.DocumentType;
import com.airlinespoo.customers.repository.ICustomerRepository;
import com.airlinespoo.customers.repository.IDocumentRepository;

import java.util.List;

public class DocumentServiceImpl implements IDocumentService {

    private final  IDocumentRepository documentRepository;
    private final ICustomerRepository customerRepository;

    public DocumentServiceImpl(
        IDocumentRepository documentRepository,
        ICustomerRepository customerRepository) {

        this.documentRepository = documentRepository;
        this.customerRepository = customerRepository;
    }

    // REGISTRAR TIPO DE DOCUMENTO
    @Override
    public void createDocument(DocumentType documentType) {

        if (documentType == null) {
            throw new IllegalArgumentException(
                "El tipo de documento no puede ser nulo."
            );
        }

        if (documentType.getId() == null ||
            documentType.getId().isBlank()) {

            throw new IllegalArgumentException(
                "El código del documento es obligatorio."
            );
        }

        if (documentType.getDescription() == null ||
            documentType.getDescription().isBlank()) {

            throw new IllegalArgumentException(
                "La descripción del documento es obligatoria."
            );
        }

        // Validar código duplicado
        if (documentRepository.existsById(documentType.getId())) {
            throw new DuplicateException(
                "tipo de documento", documentType.getId()
            );
        }

        documentRepository.create(documentType);
    }

    // BUSCAR POR ID
    @Override
    public DocumentType getDocumentById(String id) {

        return documentRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException("tipo de documento", id)
            );
    }

    // LISTAR TODOS
    @Override
    public List<DocumentType> listDocuments() {

        return documentRepository.findAll();
    }

    // ACTUALIZAR
    @Override
    public void updateDocument(DocumentType documentType) {

        if (documentType == null) {
            throw new IllegalArgumentException(
                "El tipo de documento no puede ser nulo."
            );
        }

        if (documentType.getId() == null ||
            documentType.getId().isBlank()) {

            throw new IllegalArgumentException(
                "El código del documento es obligatorio."
            );
        }

        // Verificar que exista
        DocumentType existente = documentRepository
            .findById(documentType.getId())
            .orElseThrow(
                () -> new NotFoundException(
                    "tipo de documento", documentType.getId()
                )
            );

        if (documentType.getDescription() == null ||
            documentType.getDescription().isBlank()) {

            throw new IllegalArgumentException(
                "La descripción no puede estar vacía."
            );
        }

        existente.setDescription(
            documentType.getDescription().trim()
        );

        documentRepository.update(existente);
    }

    // ELIMINAR
    @Override
    public void deleteDocument(String id) {

        documentRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException("tipo de documento", id)
            );

        // Comprobar si algún cliente utiliza este tipo
        if (customerRepository.existsByDocumentType(id)) {

            throw new BusinessRuleException(
                "No se puede eliminar el tipo de documento " +
                    id + " porque está siendo utilizado por clientes."
            );
        }

        documentRepository.deleteById(id);
    }
}
