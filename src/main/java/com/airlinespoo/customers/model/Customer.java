package com.airlinespoo.customers.model;

public class Customer {
    private static final int MAX_ID_LENGTH = 20;
    private static final int MAX_NAME_LENGTH = 30;
    private static final int MAX_TELEPHONE_LENGTH = 15;
    private static final int MAX_COUNTRY_LENGTH = 20;
    private static final int MAX_ADDRESS_LENGTH = 40;
    private static final int MAX_EMAIL_LENGTH = 80;

    private static final String TELEPHONE_REGEX = "\\+?[0-9]+";
    private static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    private final String id;
    private String firstName;
    private String lastName;
    private DocumentType documentType;
    private String telephoneNumber;
    private String email;
    private String address;
    private String country;

    public Customer(String id, String firstName, String lastName, DocumentType documentType,
                    String telephoneNumber, String country) {
        this.id = validateRequiredText(id, "El id", MAX_ID_LENGTH);
        this.firstName = validateRequiredText(firstName, "El nombre", MAX_NAME_LENGTH);
        this.lastName = validateRequiredText(lastName, "El apellido", MAX_NAME_LENGTH);
        this.documentType = validateDocumentType(documentType);
        this.telephoneNumber = validateTelephoneNumber(telephoneNumber);
        this.country = validateRequiredText(country, "El país", MAX_COUNTRY_LENGTH);
    }

    private String validateRequiredText(String value, String fieldName, int maxLength) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " es obligatorio");
        }
        String normalized = value.trim();
        if (normalized.length() > maxLength) {
            throw new IllegalArgumentException(
                fieldName + " no puede tener más de " + maxLength + " caracteres"
            );
        }
        return normalized;
    }

    private DocumentType validateDocumentType(DocumentType documentType) {
        if (documentType == null) {
            throw new IllegalArgumentException("El tipo de documento no puede ser nulo");
        }
        return documentType;
    }

    private String validateTelephoneNumber(String telephoneNumber) {
        String normalized = validateRequiredText(
            telephoneNumber, "El número de teléfono", MAX_TELEPHONE_LENGTH
        );
        if (!normalized.matches(TELEPHONE_REGEX)) {
            throw new IllegalArgumentException(
                "El número de teléfono solo puede contener dígitos y un '+' opcional al inicio"
            );
        }
        return normalized;
    }

    private String validateOptionalAddress(String address) {
        if (address == null) {
            return null;
        }
        String normalized = address.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía");
        }
        if (normalized.length() > MAX_ADDRESS_LENGTH) {
            throw new IllegalArgumentException(
                "La dirección no puede tener más de " + MAX_ADDRESS_LENGTH + " caracteres"
            );
        }
        return normalized;
    }

    private String validateOptionalEmail(String email) {
        if (email == null) {
            return null;
        }
        String normalized = email.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico no puede estar vacío");
        }
        if (normalized.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException(
                "El correo electrónico no puede tener más de " + MAX_EMAIL_LENGTH + " caracteres"
            );
        }
        if (!normalized.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("El formato del correo electrónico no es válido");
        }
        return normalized;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = validateRequiredText(firstName, "El nombre", MAX_NAME_LENGTH);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = validateRequiredText(lastName, "El apellido", MAX_NAME_LENGTH);
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = validateDocumentType(documentType);
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = validateTelephoneNumber(telephoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validateOptionalEmail(email);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = validateOptionalAddress(address);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = validateRequiredText(country, "El país", MAX_COUNTRY_LENGTH);
    }
}
