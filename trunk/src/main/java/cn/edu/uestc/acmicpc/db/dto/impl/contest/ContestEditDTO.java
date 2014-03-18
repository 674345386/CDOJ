package cn.edu.uestc.acmicpc.db.dto.impl.contest;

import java.sql.Timestamp;

/**
 * DTO post from contest editor.
 */
public class ContestEditDTO {

  public ContestEditDTO() {
  }

  public ContestEditDTO(String action, Integer contestId, String description, Integer lengthDays,
                        Integer lengthHours, Integer lengthMinutes, String problemList,
                        Timestamp time, String title, Byte type) {
    this.action = action;
    this.contestId = contestId;
    this.description = description;
    this.lengthDays = lengthDays;
    this.lengthHours = lengthHours;
    this.lengthMinutes = lengthMinutes;
    this.problemList = problemList;
    this.time = time;
    this.title = title;
    this.type = type;
  }

  private String action;
  private Integer contestId;
  private String description;
  private Integer lengthDays;
  private Integer lengthHours;
  private Integer lengthMinutes;
  private String problemList;
  private Timestamp time;
  private String title;
  private Byte type;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Integer getContestId() {
    return contestId;
  }

  public void setContestId(Integer contestId) {
    this.contestId = contestId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getLengthDays() {
    return lengthDays;
  }

  public void setLengthDays(Integer lengthDays) {
    this.lengthDays = lengthDays;
  }

  public Integer getLengthHours() {
    return lengthHours;
  }

  public void setLengthHours(Integer lengthHours) {
    this.lengthHours = lengthHours;
  }

  public Integer getLengthMinutes() {
    return lengthMinutes;
  }

  public void setLengthMinutes(Integer lengthMinutes) {
    this.lengthMinutes = lengthMinutes;
  }

  public String getProblemList() {
    return problemList;
  }

  public void setProblemList(String problemList) {
    this.problemList = problemList;
  }

  public Timestamp getTime() {
    return time;
  }

  public void setTime(Timestamp time) {
    this.time = time;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Byte getType() {
    return type;
  }

  public void setType(Byte type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ContestEditDTO that = (ContestEditDTO) o;

    if (action != null ? !action.equals(that.action) : that.action != null) return false;
    if (contestId != null ? !contestId.equals(that.contestId) : that.contestId != null)
      return false;
    if (description != null ? !description.equals(that.description) : that.description != null)
      return false;
    if (lengthDays != null ? !lengthDays.equals(that.lengthDays) : that.lengthDays != null)
      return false;
    if (lengthHours != null ? !lengthHours.equals(that.lengthHours) : that.lengthHours != null)
      return false;
    if (lengthMinutes != null ? !lengthMinutes.equals(that.lengthMinutes) : that.lengthMinutes != null)
      return false;
    if (problemList != null ? !problemList.equals(that.problemList) : that.problemList != null)
      return false;
    if (time != null ? !time.equals(that.time) : that.time != null) return false;
    if (title != null ? !title.equals(that.title) : that.title != null) return false;
    if (type != null ? !type.equals(that.type) : that.type != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = action != null ? action.hashCode() : 0;
    result = 31 * result + (contestId != null ? contestId.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (lengthDays != null ? lengthDays.hashCode() : 0);
    result = 31 * result + (lengthHours != null ? lengthHours.hashCode() : 0);
    result = 31 * result + (lengthMinutes != null ? lengthMinutes.hashCode() : 0);
    result = 31 * result + (problemList != null ? problemList.hashCode() : 0);
    result = 31 * result + (time != null ? time.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private Builder() {
    }

    private String action;
    private Integer contestId;
    private String description;
    private Integer lengthDays;
    private Integer lengthHours;
    private Integer lengthMinutes;
    private String problemList;
    private Timestamp time;
    private String title;
    private Byte type;

    public ContestEditDTO build() {
      return new ContestEditDTO(action, contestId, description, lengthDays, lengthHours, lengthMinutes,
          problemList, time, title, type);
    }

    public Builder setAction(String action) {
      this.action = action;
      return this;
    }

    public Builder setContestId(Integer contestId) {
      this.contestId = contestId;
      return this;
    }

    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder setLengthDays(Integer lengthDays) {
      this.lengthDays = lengthDays;
      return this;
    }

    public Builder setLengthHours(Integer lengthHours) {
      this.lengthHours = lengthHours;
      return this;
    }

    public Builder setLengthMinutes(Integer lengthMinutes) {
      this.lengthMinutes = lengthMinutes;
      return this;
    }

    public Builder setProblemList(String problemList) {
      this.problemList = problemList;
      return this;
    }

    public Builder setTime(Timestamp time) {
      this.time = time;
      return this;
    }

    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder setType(Byte type) {
      this.type = type;
      return this;
    }
  }
}
