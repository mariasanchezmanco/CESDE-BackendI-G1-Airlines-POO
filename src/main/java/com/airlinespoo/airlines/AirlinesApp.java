package com.airlinespoo.airlines;

import com.airlinespoo.airlines.ui.AirlineMenu;

public class AirlinesApp {
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Módulo de Aerolíneas...");
        AirlineMenu menu = new AirlineMenu();
        menu.showMenu();
    }
}
