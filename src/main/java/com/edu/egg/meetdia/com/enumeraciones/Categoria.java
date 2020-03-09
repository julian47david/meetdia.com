package com.edu.egg.meetdia.com.enumeraciones;

public enum Categoria {
    MUSICA("Música"),
    FOTOGRAFIA("Fotografía"),
    CINEYVIDEO("Cine/Video"),
    ILUMINACION("Iluminación"),
    GUIONES("Guión"),
    ANIMACION("Animación");
    
    private final String displayValue;
    
    private Categoria(String displayValue){
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
    
}
//LISTO