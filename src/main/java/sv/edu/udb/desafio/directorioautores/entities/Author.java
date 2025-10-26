package sv.edu.udb.desafio.directorioautores.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "author")
public class Author implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "autId")
    private Integer id;

    @Column(name = "nombreAutor", nullable = false)
    private String firstName;

    @Column(name = "apellidoAutor", nullable = false)
    private String lastName;

    @Temporal(TemporalType.DATE)
    @Column(name = "fechaNacimiento")
    private Date birthDate;

    @Column(name = "telefono")
    private String phone;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "generoId")
    private LiteraryGenre literaryGenre;

    public Author() {
    }

    // --- Getters y Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LiteraryGenre getLiteraryGenre() { return literaryGenre; }
    public void setLiteraryGenre(LiteraryGenre literaryGenre) { this.literaryGenre = literaryGenre; }
}