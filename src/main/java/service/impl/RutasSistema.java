package service.impl;

public final class RutasSistema {

  private static final String LISTA_NEGRA_CLAVES =
      "src/main/resources/commonCredentials/HuellaBlackList.txt";
  private static final String LISTA_TOP10K_CLAVES =
      "src/main/resources/commonCredentials/10k-most-common.txt";

  public static String listaNegraClaves() {
    return LISTA_NEGRA_CLAVES;
  }

  public static String listaTop10000Claves() {
    return LISTA_TOP10K_CLAVES;
  }
}
