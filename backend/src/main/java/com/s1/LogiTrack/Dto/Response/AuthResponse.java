package com.s1.LogiTrack.Dto.Response;

import com.s1.LogiTrack.Model.Rol;
import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private String nombre;
    private String apellido;
    private String email;
    private Rol rol;
}