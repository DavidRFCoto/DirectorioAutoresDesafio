package sv.edu.udb.desafio.directorioautores.validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.regex.Pattern;

/**
 * Validador para números telefónicos de El Salvador
 * Formato: ####-#### donde el primer dígito debe ser 2, 3, 6 o 7
 * Ejemplo: 2234-5678, 7890-1234
 */
@FacesValidator("svPhoneValidator")
public class SVPhoneValidator implements Validator<String> {

    // Expresión regular para el formato ####-#### que empieza con 2, 3, 6 o 7
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[2367]\\d{3}-\\d{4}$");

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        // Si el campo está vacío, no validar (required se encarga de eso)
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        // Validar el formato del teléfono
        if (!PHONE_PATTERN.matcher(value).matches()) {
            FacesMessage msg = new FacesMessage(
                    "Error de validación de teléfono",
                    "El formato debe ser ####-#### y empezar con 2, 3, 6 o 7. Ejemplo: 2234-5678"
            );
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
