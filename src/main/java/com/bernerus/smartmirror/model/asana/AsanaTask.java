package com.bernerus.smartmirror.model.asana;

/**
 * Created by andreas on 2017-03-01.
 */
public class AsanaTask {
  private Long id;
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AsanaTask other = (AsanaTask) o;

    if (id != null ? !id.equals(other.id) : other.id != null) return false;
    return name != null ? name.equals(other.name) : other.name == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }
}
