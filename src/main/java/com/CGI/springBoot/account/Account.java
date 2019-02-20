package com.CGI.springBoot.account;

import com.CGI.springBoot.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;

	@NotBlank
	private String mail;

	@NotBlank
	private String password;

	private String description;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER"), nullable = false)
	@JsonIgnoreProperties("accountList")
	private User user;

	public Account() {
	}

	public Account(@NotBlank String mail, @NotBlank String password, String description, User user) {
		this.mail = mail;
		this.password = password;
		this.description = description;
		this.user = user;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Account account = (Account) o;

		if (code != account.code) return false;
		if (!Objects.equals(mail, account.mail)) return false;
		if (!Objects.equals(password, account.password)) return false;
		return Objects.equals(description, account.description);
	}

	@Override
	public int hashCode() {
		int result = code != null ? code.hashCode() : 0;
		result = 31 * result + (mail != null ? mail.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "account{" +
				"code=" + code +
				", mail='" + mail + '\'' +
				", password='" + password + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
