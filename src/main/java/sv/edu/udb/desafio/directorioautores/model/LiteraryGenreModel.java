package sv.edu.udb.desafio.directorioautores.model;

import sv.edu.udb.desafio.directorioautores.entities.LiteraryGenre;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class LiteraryGenreModel {
    private static final Logger LOGGER = Logger.getLogger(LiteraryGenreModel.class.getName());

    @PersistenceContext(unitName = "AuthorsPU")
    private EntityManager em;

    public List<LiteraryGenre> getAllGenres() {
        try {
            return em.createQuery("SELECT g FROM LiteraryGenre g", LiteraryGenre.class).getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todos los generos", e);
            return null;
        }
    }

    public LiteraryGenre findGenreById(Integer id) {
        try {
            return em.find(LiteraryGenre.class, id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al buscar genero por ID", e);
            return null;
        }
    }
}