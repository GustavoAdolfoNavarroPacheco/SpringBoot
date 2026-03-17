package com.s1.LogiTrack.Dto.Response;

import com.s1.LogiTrack.Model.Rol;
import lombok.Data;

@Data
public class PersonaResponse {

    private Integer id;
    private String nombre;
    private String apellido;
    private String documento;
    private String email;
    private Rol rol;
}