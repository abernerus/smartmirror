package com.bernerus.smartmirror.model.asana;

import java.util.List;

/**
 * Created by andreas on 2017-03-01.
 */
public class AsanaUserData {
  private Long id;
  private String email;
  private String name;
  private String photo;
  private List<AsanaWorkspace> workspaces;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public List<AsanaWorkspace> getWorkspaces() {
    return workspaces;
  }

  public void setWorkspaces(List<AsanaWorkspace> workspaces) {
    this.workspaces = workspaces;
  }
}
