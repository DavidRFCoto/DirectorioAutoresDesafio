package sv.edu.udb.desafio.directorioautores.model;

import sv.edu.udb.desafio.directorioautores.entities.Author;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class AuthorModel {
    private static final Logger LOGGER = Logger.getLogger(AuthorModel.class.getName());

    @PersistenceContext(unitName = "AuthorsPU")
    private EntityManager em;

    public void createAuthor(Author author) {
        try {
            em.persist(author);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al crear autor", e);
            throw e;
        }
    }

    public void updateAuthor(Author author) {
        try {
            em.merge(author);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar autor", e);
            throw e;
        }
    }

    public void deleteAuthor(Author author) {
        try {
            Author managedAuthor = em.find(Author.class, author.getId());
            if (managedAuthor != null) {
                em.remove(managedAuthor);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar autor", e);
            throw e;
        }
    }

    public List<Author> getAllAuthors() {
        try {
            return em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todos los autores", e);
            return null;
        }
    }
}