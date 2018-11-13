package org.armstrong.abox.generation.alc;

public enum ExemplarType {
  SATISFYING("Satisfying"),
  VIOLATING("Violating");
  
  private String name;
  
  ExemplarType(String name) {
    this.name = name;
  }
  
  public String getName(){
    return name;
  }
}
