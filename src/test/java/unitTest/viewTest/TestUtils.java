package unitTest.viewTest;

import java.lang.reflect.Field;

public class TestUtils {
    /**
     * Accesses a private field of the given instance using reflection.
     *
     * @param instance  the object instance containing the field
     * @param fieldName the name of the field to access
     * @return the value of the field
     */
    public static Object getPrivateField(Object instance, String fieldName) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access private field: " + fieldName, e);
        }
    }

    /**
     * Sets a value to a private field of the given instance using reflection.
     *
     * @param instance  the object instance containing the field
     * @param fieldName the name of the field to set
     * @param value     the value to set
     */
    public static void setPrivateField(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set private field: " + fieldName, e);
        }
    }
}
