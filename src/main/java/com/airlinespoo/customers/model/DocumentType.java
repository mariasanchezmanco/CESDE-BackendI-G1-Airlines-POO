package com.airlinespoo.customers.model;
import java.util.Objects;
import com.airlinespoo.core.model.Identifiable;

public class DocumentType implements Identifiable<String> {
    private String id;
    private String description;
    //CONSTRUCTOR
    public DocumentType(String id, String description) {
        this.id=validateID(id);
        this.description=validateDescription(description);
    }
    //GET AND SET
    @Override
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = validateDescription(description);
    }

    //METODOS
    private String validateID(String id){
        //Aca se valida si id es nulo o si esta vacio
        if (id==null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Tipo de id es obligatorio");
        }
        String normalizedId = id.trim().toUpperCase();
        //Se valida que id no contenga mas de 2 caracteres
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
        if (clean.length() > 30){
            throw new IllegalArgumentException("La descripcion no puede ser mayor de " + 30);
        }
        return clean;
    }

    @Override
    public String toString() {
        return id + " - " + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj==null || getClass() != obj.getClass()){
            return false;
        }
        DocumentType other = (DocumentType) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
