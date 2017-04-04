package com.gigigo.gigigocrud_sqliteandroid.Objects;

/**
 * Created by pablo.rojas on 29/3/17.
 */

public class ModelUser {

  public String nombre;
  public String datetime;
  public int edad;
  public int id;

  public ModelUser() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ModelUser(String nombre, int edad) {
    this.nombre = nombre;
    this.edad = edad;
    this.datetime = "datetime()";

  }

  public String getNombre() {

    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getEdad() {
    return edad;
  }

  public void setEdad(int edad) {
    this.edad = edad;
  }

  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ModelUser modelUser = (ModelUser) o;

    if (edad != modelUser.edad) return false;
    return nombre != null ? nombre.equals(modelUser.nombre) : modelUser.nombre == null;
  }

  @Override public int hashCode() {
    int result = nombre != null ? nombre.hashCode() : 0;
    result = 31 * result + edad;
    return result;
  }
}
