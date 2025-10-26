
package sv.edu.udb.desafio.directorioautores.beans;

import sv.edu.udb.desafio.directorioautores.entities.Author;
import sv.edu.udb.desafio.directorioautores.entities.LiteraryGenre;
import sv.edu.udb.desafio.directorioautores.model.AuthorModel;
import sv.edu.udb.desafio.directorioautores.model.LiteraryGenreModel;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Named("authorBean")
@ViewScoped
public class AuthorBean implements Serializable {

    @Inject
    private AuthorModel authorModel;

    @Inject
    private LiteraryGenreModel genreModel;

    @Inject
    private LiteraryGenreBean literaryGenreBean;

    private Author author = new Author();
    private List<Author> authors;
    private String message;

    private List<Author> filteredAuthors;
    private Integer selectedGenreFilterId;

    @PostConstruct
    public void init() {
        loadAuthors();
    }

    private void loadAuthors() {
        authors = authorModel.getAllAuthors();
        filteredAuthors = authors;
    }

    public void addAuthor() {
        if (!validateAuthor()) {
            return;
        }

        Integer selectedGenreId = literaryGenreBean.getSelectedGenreId();
        if (selectedGenreId != null) {
            LiteraryGenre selectedGenre = genreModel.findGenreById(selectedGenreId);
            author.setLiteraryGenre(selectedGenre);
        }

        try {
            if (author.getId() == null) {
                authorModel.createAuthor(author);
                message = "Autor agregado correctamente.";
            } else {
                authorModel.updateAuthor(author);
                message = "Autor actualizado correctamente.";
            }
        } catch (Exception e) {
            message = "Error: El autor ya existe (mismo nombre y apellido).";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return;
        }

        author = new Author();
        literaryGenreBean.setSelectedGenreId(null);
        loadAuthors();

        // Mantener el filtro aplicado después de agregar/editar
        applyGenreFilter();
    }

    private boolean validateAuthor() {
        boolean isValid = true;
        if (author.getFirstName() == null || author.getFirstName().trim().isEmpty()) {
            addErrorMessage("El campo Nombre es obligatorio.");
            isValid = false;
        }
        if (author.getLastName() == null || author.getLastName().trim().isEmpty()) {
            addErrorMessage("El campo Apellido es obligatorio.");
            isValid = false;
        }
        if (author.getBirthDate() != null) {
            LocalDate birthDate = author.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
                addErrorMessage("El autor debe ser mayor de 18 años.");
                isValid = false;
            }
        } else {
            addErrorMessage("La fecha de nacimiento es obligatoria.");
            isValid = false;
        }
        return isValid;
    }

    private void addErrorMessage(String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null));
    }

    public void editAuthor(Author authorToEdit) {
        if(authorToEdit==null){
            addErrorMessage("No se puede seleccionar el autor para seleccionar.");
            return;
        }
        this.author = authorToEdit;
        if (authorToEdit.getLiteraryGenre() != null) {
            literaryGenreBean.setSelectedGenreId(authorToEdit.getLiteraryGenre().getId());
        } else {
            literaryGenreBean.setSelectedGenreId(null);
        }
    }

    public void deleteAuthor(Author authorToDelete) {
        try {
            authorModel.deleteAuthor(authorToDelete);
            message = "Autor eliminado correctamente.";
        } catch(Exception e) {
            message = "Error al eliminar el autor.";
        }
        loadAuthors();
        applyGenreFilter();
    }

    public void applyGenreFilter() {
        if (selectedGenreFilterId == null) {
            filteredAuthors = authors;
        } else {
            filteredAuthors = authors.stream()
                    .filter(a -> a.getLiteraryGenre() != null &&
                            a.getLiteraryGenre().getId().equals(selectedGenreFilterId))
                    .collect(Collectors.toList());
        }
    }

    // --- Getters y Setters ---

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
    public List<Author> getAuthors() { return authors; }
    public void setAuthors(List<Author> authors) { this.authors = authors; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<Author> getFilteredAuthors() { return filteredAuthors; }
    public void setFilteredAuthors(List<Author> filteredAuthors) { this.filteredAuthors = filteredAuthors; }
    public Integer getSelectedGenreFilterId() { return selectedGenreFilterId; }
    public void setSelectedGenreFilterId(Integer selectedGenreFilterId) { this.selectedGenreFilterId = selectedGenreFilterId; }
}