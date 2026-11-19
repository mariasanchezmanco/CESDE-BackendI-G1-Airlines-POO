package com.airlinespoo.customers.ui;

import com.airlinespoo.customers.model.DocumentType;

public class test {
    public static void main(String[] args) {
        DocumentType a = new DocumentType("cc", "Cédula");
        DocumentType b = new DocumentType("CC", "Otra");
        System.out.println(a.equals(b));                  // true
        System.out.println(a.hashCode() == b.hashCode()); // true

    }
}
