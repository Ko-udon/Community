package community.project.community.client.enums;

public enum UserRole {
  ADMIN("ROLE_ADMIN"),
  USER("ROLE_USER");

  private String userRole;

  UserRole(String userRole) {
    this.userRole = userRole;
  }

}
