package com.emumba.blogapi.model.user;



import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;


    @Column(nullable = true)
    @Email(message = "Invalid email format", groups = NonGitHubUserValidation.class)
    @NotBlank(message = "Email is required", groups = NonGitHubUserValidation.class)
    private String email;

    @Column(nullable = true)
    @NotBlank(message = "Password is required", groups = NonGitHubUserValidation.class)
    @Size(min = 6, message = "Password must be at least 6 characters long", groups = NonGitHubUserValidation.class)
    private String password;

    // Additional fields like roles can be added later

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), // Foreign key referring to User's id
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") // Foreign key referring to Role's id
    )
    private Set<Role> roles = new HashSet<>();

    private Long githubId; // To store the GitHub ID
    private Boolean isGithubUser;
    private String accessToken;
}

