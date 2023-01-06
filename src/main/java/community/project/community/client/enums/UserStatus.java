package community.project.community.client.enums;

public enum UserStatus {
  REQ("REQUEST_STATUS"),
  ING("ACTIVATE_STATUS"),
  STOP("STOP_STATUS");

  private String userStatus;

  UserStatus(String userRole) {
    this.userStatus = userRole;
  }
}
