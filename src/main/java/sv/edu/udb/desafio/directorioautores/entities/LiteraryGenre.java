package sv.edu.udb.desafio.directorioautores.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "literarygenre")
public class LiteraryGenre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genId")
    private Integer id;

    @Column(name = "nombreGenero", nullable = false, unique = true)
    private String name;

    @Column(name = "descripcionGenero")
    private String description;

    @OneToMany(mappedBy = "literaryGenre")
    private List<Author> authors;

    public LiteraryGenre() {
    }

    // --- Getters y Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Author> getAuthors() { return authors; }
    public void setAuthors(List<Author> authors) { this.authors = authors; }
}