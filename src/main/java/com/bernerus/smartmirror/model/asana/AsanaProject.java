package com.bernerus.smartmirror.model.asana;

import java.util.List;

/**
 * Created by andreas on 2017-03-01.
 */
public class AsanaProject {
  private List<AsanaProjectData> datas;

  public List<AsanaProjectData> getDatas() {
    return datas;
  }

  public void setDatas(List<AsanaProjectData> datas) {
    this.datas = datas;
  }
}
