package com.bernerus.smartmirror.model.asana;

import java.util.List;

/**
 * Created by andreas on 2017-03-01.
 */
public class AsanaTasks {
  private List<AsanaTask> data;

  public List<AsanaTask> getData() {
    return data;
  }

  public void setData(List<AsanaTask> data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AsanaTasks other = (AsanaTasks) o;
    if(this.getData().size() == other.getData().size()) {
      for(int i = 0; i < this.getData().size(); i++){
        AsanaTask thisTask = this.getData().get(i);
        AsanaTask otherTask = other.getData().get(i);
        if (!thisTask.equals(otherTask)) {
          return false;
        }
      }
    } else {
      return false;
    }

    return data != null ? data.equals(other.data) : other.data == null;
  }

  @Override
  public int hashCode() {
    return data != null ? data.hashCode() : 0;
  }
}
