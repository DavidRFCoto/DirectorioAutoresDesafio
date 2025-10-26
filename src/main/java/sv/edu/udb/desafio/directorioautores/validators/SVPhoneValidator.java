package sv.edu.udb.desafio.directorioautores.validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator("svPhoneValidator") // ID para usarlo en la vista
public class SVPhoneValidator implements Validator {

    // Expresión regular para el formato ####-#### que empieza con 2, 6 o 7.
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[267]\\d{3}-\\d{4}$");

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null || value.toString().isEmpty()) {
            return; // No validar si está vacío, para eso se usa required="true"
        }

        String phone = value.toString();
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            FacesMessage msg = new FacesMessage("Error de validación de teléfono",
                    "El formato debe ser ####-#### y empezar con 2, 6 o 7.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}