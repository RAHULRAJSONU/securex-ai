package io.github.rahulrajsonu.securexai.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name","namespace", "objectId"})})
public class RoleHierarchy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String expression;
    private String namespace;
    private String objectId;
    private boolean isActive;

    public void validate(){
        Objects.requireNonNull(this.expression, "Role hierarchy expression is required.");
        String regex = "^[A-Za-z]+( > [A-Za-z]+)*$";
        Pattern pattern = java.util.regex.Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.expression);
        if(!matcher.matches()){
            throw new RuntimeException("""
            Invalid role hierarchy expression.
            - Expression should only contains A-Za-z and '>'
            - Two role must be separated by '>'
                valid:
                   Admin > Editor > Member > Viewer  > Reader
                   Owner > Admin > Editor > Member > Viewer  > Reader
                   Auditor > Member > Viewer  > Reader
                Invalid:
                   Admin Editor > Member > Viewer > Reader
                   Owner < Admin > Editor > Member > Viewer > Reader
                   8_Invalid > Role > Hierarchy!
                   Admin > > Member
            """);
        }
    }
}