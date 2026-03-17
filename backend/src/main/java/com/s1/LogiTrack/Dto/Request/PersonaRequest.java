package com.s1.LogiTrack.Dto.Request;

import com.s1.LogiTrack.Model.Rol;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PersonaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El documento es obligatorio")
    private String documento;

    @Email(message = "El email debe ser valido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
}