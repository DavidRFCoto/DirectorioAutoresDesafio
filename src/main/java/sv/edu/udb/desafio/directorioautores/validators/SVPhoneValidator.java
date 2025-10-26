package sv.edu.udb.desafio.directorioautores.validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.regex.Pattern;

/**
 * Validador para numeros telefonicos de El Salvador.
 * Formato valido: ####-####
 * Debe empezar con 2, 3, 6 o 7.
 */
@FacesValidator("svPhoneValidator")
public class SVPhoneValidator implements Validator<String> {

    // Expresión regular: ####-####, primer dígito 2,3,6 o 7
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[2367]\\d{3}-\\d{4}$");

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {

        // Evaluar si el campo esta vacio
        if (value == null || value.trim().isEmpty()) {
            throw new ValidatorException(
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "El numero de telefono es obligatorio.",
                            "Debe ingresar un numero de telefono.")
            );
        }

        // Si el formato es incorrecto
        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw new ValidatorException(
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Formato de telefono invalido.",
                            "Debe seguir el formato ####-#### y empezar con 2, 3, 6 o 7. Ejemplo: 7234-5678")
            );
        }
    }
}



