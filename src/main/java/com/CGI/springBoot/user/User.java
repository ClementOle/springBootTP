package com.CGI.springBoot.user;

import com.CGI.springBoot.account.Account;
import com.CGI.springBoot.group.Group;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	private String nickName;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	@JsonIgnoreProperties("user")
	private List<Account> accountList = new ArrayList<>();


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "FK_GROUP"), nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnoreProperties("userList")
	private Group group;

	public User() {
	}

	public User(@NotBlank String firstName, @NotBlank String lastName, String nickName, List<Account> accountList, Group group) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.accountList = accountList;
		this.group = group;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public List<Account> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (!Objects.equals(id, user.id)) return false;
		if (!Objects.equals(firstName, user.firstName)) return false;
		if (!Objects.equals(lastName, user.lastName)) return false;
		return Objects.equals(nickName, user.nickName);
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", nickName='" + nickName + '\'' +
				", accountList=" + accountList +
				", group=" + group +
				'}';
	}
}
