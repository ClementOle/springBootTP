package com.CGI.springBoot.group;

import com.CGI.springBoot.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "groupDemo")
@Getter
@Setter
@NoArgsConstructor
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank
	private String title;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "group")
	@JsonIgnoreProperties("group")
	private List<User> userList = new ArrayList<>();

	public Group(@NotBlank String title, List<User> userList) {
		this.title = title;
		this.userList = userList;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Group group = (Group) o;

		if (!id.equals(group.id)) return false;
		return Objects.equals(title, group.title);
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "group{" +
				"id=" + id +
				", title='" + title + '\'' +
				'}';
	}
}
