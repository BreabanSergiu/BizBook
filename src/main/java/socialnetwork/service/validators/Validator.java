package socialnetwork.service.validators;

import socialnetwork.domain.validators.ValidationException;

public interface Validator<T> {
    /**
     * validates the friendship addition operation
     * @param entity
     *               entity to be validate
     * @throws ValidationException
     *                     if entity already exist
     */
    void validateAdd(T entity) throws ValidationException;


    /**
     * validates the friendship wiping operation
     * @param entity
     *               entity to be validate
     * @throws ValidationException
     *                  if entity to be deleted doesn t exist
     */
    void validateDelete (T entity) throws ValidationException;
}
