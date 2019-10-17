package com.zookeeper.model;

public class User {

   private int id;
   private String firstName;
   private String lastName;

   private String email;

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder("User{");
      sb.append("id=").append(id);
      sb.append(", firstName='").append(firstName).append('\'');
      sb.append(", lastName='").append(lastName).append('\'');
      sb.append(", email='").append(email).append('\'');
      sb.append('}');
      return sb.toString();
   }
}
