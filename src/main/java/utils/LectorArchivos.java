package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LectorArchivos {
  private String path;

  public LectorArchivos(String path) {
    this.path = path;
  }

  public Boolean pathArchivoValido() {
    File test = new File(path);
    return test.exists();
  }

  private InputStreamReader abrirArchivo(String path) throws FileNotFoundException {
    File file = new File(path).getAbsoluteFile();
    return new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
  }

  public Boolean existeEnArchivo(String cadena) throws IOException {
    String linea;
    InputStreamReader fileReader = abrirArchivo(this.path);

    BufferedReader bufferedReader = new BufferedReader(fileReader);
    while ((linea = bufferedReader.readLine()) != null) {
      if (linea.equals(cadena)) {
        bufferedReader.close();
        return Boolean.TRUE;
      }
    }
    bufferedReader.close();
    return Boolean.FALSE;
  }
}
