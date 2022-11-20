package service.impl;

import domain.exception.BaseCredencialesNoDisponibleException;
import domain.exception.PasswordDebilException;
import java.io.IOException;

import service.ValidacionClave;
import utils.LectorArchivos;

public class ValidadorClaveTop10000 implements ValidacionClave {

  private final String path = RutasSistema.listaTop10000Claves();
  private LectorArchivos lector = new LectorArchivos(path);

  public void validarClave(String usuario, String password) {
    try {
      if (lector.existeEnArchivo(password)) {
        throw new PasswordDebilException("La password ingresada es debil.");
      }
    } catch (IOException e) {
      throw new BaseCredencialesNoDisponibleException(e.getMessage());
    }
  }
}