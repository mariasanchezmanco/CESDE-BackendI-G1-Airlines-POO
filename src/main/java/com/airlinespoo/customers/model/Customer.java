package com.airlinespoo.customers.model;

import java.util.Objects;

public class Customer {
    private static final int MAX_MAIL_LENGTH=80;
    private static final int MAX_ADDRES_LENGTH=40;
    private static final String TELEPHONE_REGEX = "\\+?[0-9]+";
    private static final String MAIL_REGEX="[^@\\s]+@[^@\\\\s]+\\.[^@\\s]+";
    private static final int MAX_NAME_LENGTH=30;
    private static final int MAX_ID_LENGTH=20;
    private static final int MAX_CEL_NUMBER_LENGTH=15;
    private static final int MAX_COUNTRY_LENGTH=20;
    private String id;
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
        this.telephoneNumber = validateNumber(telephoneNumber);
        this.country = validateCountry(country);
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
        this.documentType = validateDocument(documentType);
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = validateNumber(telephoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validateMail(email);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = validateAddres(address);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = validateCountry(country);
    }
    public String getFullName(){
        return firstName + " "+ lastName;
    }

    private String validateName(String data){
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
        if (id==null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Id es obligatorio");
        }
        String normalizedId = id.trim();
        if(normalizedId.length() >MAX_ID_LENGTH){
            throw new IllegalArgumentException("El id no debe tener mas de "+MAX_ID_LENGTH +"caracteres");
        }
        return  normalizedId;
    }
    private String validateNumber(String data){
        if (data==null || data.trim().isEmpty()){
            throw new IllegalArgumentException("El telefono es obligatorio");
        }
        String normalizedData = data.trim();
        if(normalizedData.length() >MAX_CEL_NUMBER_LENGTH){
            throw new IllegalArgumentException("El telefono no debe tener mas de "+ MAX_CEL_NUMBER_LENGTH+ " caracteres");
        }
        if (normalizedData.matches(TELEPHONE_REGEX)){
            return normalizedData;
        }else {
            throw new IllegalArgumentException("Telefono invalido");
        }
    }
    private String validateCountry(String data){
        if (data==null || data.trim().isEmpty()){
            throw new IllegalArgumentException("Pais es obligatorio");
        }
        String normalizedData = data.trim();
        if(normalizedData.length() >MAX_COUNTRY_LENGTH){
            throw new IllegalArgumentException("El pais no debe tener mas de " +MAX_COUNTRY_LENGTH + "caracteres");
        }
        return  normalizedData;
    }
    private String validateAddres(String data){
        if (data==null || data.trim().isEmpty()){
            return data;
        }
        String normalizedData = data.trim();
        if(normalizedData.length() > MAX_ADDRES_LENGTH ){
            throw new IllegalArgumentException("Direccion no debe tener mas de "+MAX_ADDRES_LENGTH+" caracteres");
        }
        return  normalizedData;
    }
    private String validateMail(String data){
        if (data==null || data.trim().isEmpty()){
            return data;
        }
        String normalizedData = data.trim();
        if(normalizedData.length() > MAX_MAIL_LENGTH ){
            throw new IllegalArgumentException("Correo no debe tener mas de "+ MAX_MAIL_LENGTH +" caracteres");
        }if (normalizedData.matches(MAIL_REGEX)){
            return normalizedData;
        }else{
            throw new IllegalArgumentException("El correo no es valido");
        }
    }

    @Override
    public String toString() {
        return id + " - " + getFullName();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj==null || getClass() != obj.getClass()){
            return false;
        }
        Customer other = (Customer) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

