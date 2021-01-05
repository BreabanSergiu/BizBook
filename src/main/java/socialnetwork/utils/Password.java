package socialnetwork.utils;

public class Password {

    private static int workload = 12;

    /**
     *
     * @param password_plaintext The account's plaintext password as provided during account creation,
     * 	 *			     or when changing an account's password.
     * @return String - a string of length 60 that is the bcrypt hashed password in crypt(3) format.
     */

    public static String hashPassword(String password_plaintext){
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password_plaintext,salt);

        return (hashed_password);
    }

    /**
     *
     * @param password_plaintext The account's plaintext password, as provided during a login request
     * @param stored_hash The account's stored password hash, retrieved from the authorization database
     * @return boolean - true if the password matches the password of the stored hash, false otherwise
     */
    public static boolean checkPassword(String password_plaintext,String stored_hash) throws IllegalAccessException {
        boolean password_verified = false;
        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalAccessException("invalid hash provided for comparison");

        return BCrypt.checkpw(password_plaintext,stored_hash);
    }


}
