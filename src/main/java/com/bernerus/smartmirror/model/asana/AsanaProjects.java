package com.bernerus.smartmirror.model.asana;

import java.util.List;

/**
 * Created by andreas on 2017-03-01.
 */
public class AsanaProjects {
  private List<AsanaProjectData> data;

  public List<AsanaProjectData> getData() {
    return data;
  }

  public void setData(List<AsanaProjectData> data) {
    this.data = data;
  }
}
