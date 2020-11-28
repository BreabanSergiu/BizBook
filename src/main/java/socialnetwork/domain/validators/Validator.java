package socialnetwork.domain.validators;

public interface Validator<T> {
    /**
     * validates an entity
     * @param entity
     *          entity to be validate
     * @throws ValidationException
     *          if does not meet the criteria validation
     */
    void validate(T entity) throws ValidationException;
}