package sv.edu.udb.desafio.directorioautores.beans;

import sv.edu.udb.desafio.directorioautores.entities.LiteraryGenre;
import sv.edu.udb.desafio.directorioautores.model.LiteraryGenreModel;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named // Permite que JSF pueda encontrar este bean con el nombre "literaryGenreBean"
@ViewScoped // El bean vivirá mientras estés en la misma página.
public class LiteraryGenreBean implements Serializable {

    @Inject // Inyección de dependencias: El servidor nos da una instancia de LiteraryGenreModel
    private LiteraryGenreModel genreModel;

    private List<LiteraryGenre> genres;
    private Integer selectedGenreId;

    @PostConstruct // Este método se ejecuta automáticamente después de que el bean es creado
    public void init() {
        loadGenres();
    }

    private void loadGenres() {
        genres = genreModel.getAllGenres();
    }

    // --- Getters y Setters para que la vista pueda acceder a los datos ---

    public List<LiteraryGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<LiteraryGenre> genres) {
        this.genres = genres;
    }

    public Integer getSelectedGenreId() {
        return selectedGenreId;
    }

    public void setSelectedGenreId(Integer selectedGenreId) {
        this.selectedGenreId = selectedGenreId;
    }
}