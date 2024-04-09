package infrastructure.threadlocals;


import infrastructure.threadlocals.abstracts.ThreadLocalAbstract;

/**
 * Handles email of test user
 */
public class ThreadLocalEmail {

    private static final ThreadLocalAbstract<String> userCreated = new ThreadLocalAbstract<>("Email for created user") {};
    private static final ThreadLocalAbstract<String> userSignedIn = new ThreadLocalAbstract<>("Email for signed-in user") {};
    private static final ThreadLocalAbstract<String> userRemoved = new ThreadLocalAbstract<>("Email for removed user") {};

    //* * * * * Created user * * * * *//

    public static void putEmailForCreatedUser(String value) {
        userCreated.put(value);
    }

    public static String getEmailForCreatedUser() {
        return userCreated.get();
    }

    public static void clearEmailForCreatedUser() {
        userCreated.clear();
    }

    //* * * * * Signed-in user * * * * *//

    public static void putEmailForSignedInUser(String value) {
        userSignedIn.put(value);
    }

    public static String getEmailForSignedInUser() {
        return userSignedIn.get();
    }

    public static void clearEmailForSignedInUser() {
        userSignedIn.clear();
    }

    //* * * * * Removed user * * * * *//

    public static void putEmailForRemovedUser(String value) {
        userRemoved.put(value);
    }

    public static String getEmailForRemovedUser() {
        return userRemoved.get();
    }

    public static void clearEmailForRemovedUser() {
        userRemoved.clear();
    }

    //* * * * * * General * * * * * *//

    public static void clear() {
        clearEmailForCreatedUser();
        clearEmailForSignedInUser();
        clearEmailForRemovedUser();
    }
}
