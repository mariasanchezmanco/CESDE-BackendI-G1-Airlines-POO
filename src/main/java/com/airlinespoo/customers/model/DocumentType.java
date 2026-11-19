package com.airlinespoo.customers.model;

public class DocumentType {
    private String id;
    private String description;

    public DocumentType(String id, String description) {
        this.id=validateID(id);
        this.description=validateDescription(description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = validateID(id);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = validateDescription(description);
    }

    //METODOS
    private String validateID(String id){
        if (id==null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Tipo de id es obligatorio");
        }
        String normalizedId = id.trim().toUpperCase();
        if(normalizedId.length() != 2){
            throw new IllegalArgumentException("El tipo de id debe tener 2 caracteres");
        }
        return  normalizedId;
    }
    private String validateDescription(String description){
        if (description == null || description.trim().isEmpty()){
            throw new IllegalArgumentException("La descripcion es obligatoria");
        }
        String clean=description.trim();
        if (clean.length() > 20){
            throw new IllegalArgumentException("La descripcion no puede ser mayor de " + 20);
        }
        return clean;
    }
}
