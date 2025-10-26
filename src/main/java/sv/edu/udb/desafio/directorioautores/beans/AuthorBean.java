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

/**
 * Managed Bean para gestionar las operaciones CRUD de autores
 */
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
    private String searchText; // Filtro de busqueda por texto

    @PostConstruct
    public void init() {
        loadAuthors();
    }


    /** Carga todos los autores desde la base de datos **/

    private void loadAuthors() {
        authors = authorModel.getAllAuthors();
        filteredAuthors = authors;
    }

    /**
     * Agrega o actualiza un autor
     * Valida si el autor ya existe (mismo nombre y apellido) pero permite agregarlo
     */
    public void addAuthor() {
        // Limpiar mensajes previos
        message = null;

        // Validar datos del formulario
        if (!validateAuthor()) {
            return;
        }

        // Asignar el genero literario seleccionado
        Integer selectedGenreId = literaryGenreBean.getSelectedGenreId();
        if (selectedGenreId != null) {
            LiteraryGenre selectedGenre = genreModel.findGenreById(selectedGenreId);
            author.setLiteraryGenre(selectedGenre);
        }

        try {
            // Verificar si el autor ya existe mismo nombre y apellido
            boolean authorExists = checkIfAuthorExists(author);

            if (author.getId() == null) {
                // Crear nuevo autor
                authorModel.createAuthor(author);

                if (authorExists) {
                    message = "ADVERTENCIA: Autor con el mismo nombre y apellido ya existe. Se agregó de todas formas.";
                    addWarningMessage(message);
                } else {
                    message = "Autor agregado correctamente.";
                }
            } else {
                // Actualizar autor existente
                authorModel.updateAuthor(author);
                message = "Autor actualizado correctamente.";
            }
        } catch (Exception e) {
            message = "Error al guardar el autor: " + e.getMessage();
            addErrorMessage(message);
            return;
        }

        // Limpiar formulario y recargar datos
        resetForm();
        loadAuthors();
        applyFilters(); // Aplicar filtros despues de agregar/actualizar
    }

    /**
     * Verifica si ya existe un autor con el mismo nombre y apellido
     */
    private boolean checkIfAuthorExists(Author newAuthor) {
        if (authors == null) return false;

        return authors.stream()
                .filter(a -> !a.getId().equals(newAuthor.getId())) // Excluir el mismo autor si esta editando
                .anyMatch(a ->
                        a.getFirstName().equalsIgnoreCase(newAuthor.getFirstName()) &&
                                a.getLastName().equalsIgnoreCase(newAuthor.getLastName())
                );
    }

    /**
     * Valida los datos del autor antes de guardar
     */
    private boolean validateAuthor() {
        boolean isValid = true;

        // Validar nombre
        if (author.getFirstName() == null || author.getFirstName().trim().isEmpty()) {
            addErrorMessage("El campo Nombre es obligatorio.");
            isValid = false;
        }

        // Validar apellido
        if (author.getLastName() == null || author.getLastName().trim().isEmpty()) {
            addErrorMessage("El campo Apellido es obligatorio.");
            isValid = false;
        }

        // Validar fecha de nacimiento
        if (author.getBirthDate() != null) {
            LocalDate birthDate = author.getBirthDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            int age = Period.between(birthDate, LocalDate.now()).getYears();

            if (age < 18) {
                addErrorMessage("El autor debe ser mayor de 18 años. Edad actual: " + age + " años.");
                isValid = false;
            }
        } else {
            addErrorMessage("La fecha de nacimiento es obligatoria.");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Prepara el formulario para editar un autor existente
     */
    public void editAuthor(Author authorToEdit) {
        if (authorToEdit == null) {
            addErrorMessage("No se puede seleccionar el autor para editar.");
            return;
        }

        this.author = authorToEdit;

        // Seleccionar el genero en el dropdown
        if (authorToEdit.getLiteraryGenre() != null) {
            literaryGenreBean.setSelectedGenreId(authorToEdit.getLiteraryGenre().getId());
        } else {
            literaryGenreBean.setSelectedGenreId(null);
        }

        message = "Editando autor: " + authorToEdit.getFirstName() + " " + authorToEdit.getLastName();
    }

    /**
     * Elimina un autor de la base de datos
     */
    public void deleteAuthor(Author authorToDelete) {
        try {
            authorModel.deleteAuthor(authorToDelete);
            message = "Autor eliminado correctamente: " + authorToDelete.getFirstName() + " " + authorToDelete.getLastName();
        } catch (Exception e) {
            message = "Error al eliminar el autor: " + e.getMessage();
            addErrorMessage(message);
        }

        loadAuthors();
        applyGenreFilter();
    }

    /**
     * Aplica el filtro por genero literario operacion AJAX
     */
    public void applyGenreFilter() {
        applyFilters();
    }

    /**
     * Aplica el filtro de busqueda por texto operacion AJAX sincrona
     */
    public void applySearchFilter() {
        applyFilters();
    }

    /**
     * Aplica ambos filtros: genero y busqueda de texto
     */
    private void applyFilters() {
        if (authors == null) {
            filteredAuthors = null;
            return;
        }

        // Comenzar con todos los autores
        filteredAuthors = authors;

        // Aplicar filtro por genero
        if (selectedGenreFilterId != null) {
            filteredAuthors = filteredAuthors.stream()
                    .filter(a -> a.getLiteraryGenre() != null &&
                            a.getLiteraryGenre().getId().equals(selectedGenreFilterId))
                    .collect(Collectors.toList());
        }

        // Aplicar filtro de busqueda por texto
        if (searchText != null && !searchText.trim().isEmpty()) {
            String searchLower = searchText.toLowerCase().trim();
            filteredAuthors = filteredAuthors.stream()
                    .filter(a -> {
                        String fullName = (a.getFirstName() + " " + a.getLastName()).toLowerCase();
                        return fullName.contains(searchLower) ||
                                (a.getFirstName() != null && a.getFirstName().toLowerCase().contains(searchLower)) ||
                                (a.getLastName() != null && a.getLastName().toLowerCase().contains(searchLower));
                    })
                    .collect(Collectors.toList());
        }
    }

    /**
     * Limpia el formulario despues de agregar/actualizar
     */
    private void resetForm() {
        author = new Author();
        literaryGenreBean.setSelectedGenreId(null);
    }

    /**
     * Agrega un mensaje de error al contexto de JSF
     */
    private void addErrorMessage(String summary) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null));
    }

    /**
     * Agrega un mensaje de advertencia al contexto de JSF
     */
    private void addWarningMessage(String summary) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, summary, null));
    }

    // ==================== Getters y Setters ====================

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Author> getFilteredAuthors() {
        return filteredAuthors;
    }

    public void setFilteredAuthors(List<Author> filteredAuthors) {
        this.filteredAuthors = filteredAuthors;
    }

    public Integer getSelectedGenreFilterId() {
        return selectedGenreFilterId;
    }

    public void setSelectedGenreFilterId(Integer selectedGenreFilterId) {
        this.selectedGenreFilterId = selectedGenreFilterId;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
