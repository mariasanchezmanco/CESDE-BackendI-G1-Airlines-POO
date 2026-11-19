package com.airlinespoo.customers.model;

public class Customer {
    private final String id;
    private String firstName;
    private String lastName;
    private DocumentType documentType;
    private String telephoneNumber;
    private String email;
    private String address;
    private String country;

    public Customer(String id, String firstName, String lastname, DocumentType documentType,
                    String telephoneNumber, String country) {
        this.id = validateID(id);
        this.firstName = validateName(firstName);
        this.lastName = validateName(lastname);
        this.documentType = validateDocument(documentType);
        this.telephoneNumber = telephoneNumber;
        this.country = country;
    }

    private String validateName(String data){
        int MAX_NAME_LENGTH=30;
        if ( data == null || data.trim().isEmpty()){
            throw new IllegalArgumentException("Debe existir nombre o apellido");
        }
        String clearData=data.trim();
        if(clearData.length() > MAX_NAME_LENGTH){
            throw new IllegalArgumentException("Nombre y/o apellido no puede ser mayor a 30");
        }

        return clearData;
    }
    private DocumentType validateDocument(DocumentType data){
        if (data == null){
            throw new IllegalArgumentException("DocumentType no puede ser nulo");
        }
        return data;
    }
    private String validateID(String id){
        int MAX_ID_LENGTH=20;
        if (id==null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Id es obligatorio");
        }
        String normalizedId = id.trim();
        if(normalizedId.length() >MAX_ID_LENGTH){
            throw new IllegalArgumentException("El id debe tener 20 caracteres");
        }
        return  normalizedId;
    }
    private String validateNull(String data){
        if(data==null || data.trim().isEmpty()){
            throw new IllegalArgumentException("");
        }
        return ;
    }
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = validateName(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastname) {
        this.lastName = validateName(lastname);
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}

